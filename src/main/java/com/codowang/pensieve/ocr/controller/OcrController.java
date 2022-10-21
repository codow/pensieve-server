package com.codowang.pensieve.ocr.controller;

import com.baidu.aip.ocr.AipOcr;
import com.codowang.pensieve.core.utils.DateUtils;
import com.codowang.pensieve.core.utils.StringUtils;
import com.codowang.pensieve.entity.WebResult;
import com.codowang.pensieve.ocr.config.OcrConfig;
import com.codowang.pensieve.ocr.enums.BaiduOcrType;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 文字识别接口
 *
 * @author wangyb
 */
@Slf4j
@RestController
@RequestMapping("/ocr")
public class OcrController {

    private AipOcr baiduOcrClient;

    private final OcrConfig ocrConfig;

    public OcrController (OcrConfig ocrConfig) {
        this.ocrConfig = ocrConfig;
    }

    @RequestMapping("/file/upload")
    public WebResult uploadFile (@RequestParam(name = "file") MultipartFile tempFile) throws Exception {
        // 定义返回信息
        List<Map<String, Object>> nAccessoryInfoList = new LinkedList<>();

        // 取文件存储地址
        String folderPath = this.ocrConfig.getStoragePath();
        File nFolder = new File(folderPath);

        if (!nFolder.exists() && !nFolder.mkdirs()) {
            throw new Exception("创建保存目录失败");
        }

        // 提取信息，并执行写操作
        Map<String, Object> nAccessoryInfo = new HashMap<>();
        String fileName = this.calcFileName(nFolder, tempFile.getOriginalFilename() == null ? tempFile.getName() : tempFile.getOriginalFilename());
        // 保存文件
        File nFile = new File(folderPath + File.separator + fileName);
        tempFile.transferTo(nFile);
        // 组装返回参数
        nAccessoryInfo.put("name", fileName);
        nAccessoryInfo.put("size", tempFile.getSize());
        nAccessoryInfo.put("path", "/ocr/file/download/" + fileName);
        nAccessoryInfo.put("modify_time", DateUtils.getString(nFile.lastModified(), DateUtils.DATETIME_FORMAT));
        nAccessoryInfoList.add(nAccessoryInfo);
        // 返回数据
        return WebResult.success(nAccessoryInfoList);
    }

    private String calcFileName (File dir, String fileName) {
        String suffix = "";
        if (fileName.contains(".")) {
            int index = fileName.lastIndexOf(".");
            suffix = fileName.substring(index);
            fileName = fileName.substring(0, index);
        }
        if (dir == null || !dir.exists()) {
            return fileName + suffix;
        }
        if (StringUtils.isBlank(fileName)) {
            return fileName + suffix;
        }
        Pattern p = Pattern.compile("^" + fileName + "(\\(\\d+\\))?" + suffix + "$", Pattern.CASE_INSENSITIVE);
        File[] files = dir.listFiles((file, name) -> p.matcher(name).find());
        if (files == null || files.length == 0) {
            return fileName + suffix;
        }
        List<File> fileList = Arrays.stream(files).sorted(Comparator.comparing(File::lastModified)).collect(Collectors.toList());
        // 取最大数字
        File lastFile = fileList.get(fileList.size() - 1);
        Matcher matcher = p.matcher(lastFile.getName());
        if (matcher.find()) {
            String group = matcher.group(1);
            if (StringUtils.isNotBlank(group)) {
                return fileName + "(" + (Integer.parseInt(group.replace("(", "").replace(")", "")) + 1) + ")" + suffix;
            }
        }
        return fileName + "(1)" + suffix;
    }

    @RequestMapping("/file/remove/{fileName}")
    public WebResult removeFile (@PathVariable String fileName) throws Exception {
        String path = this.ocrConfig.getStoragePath() + File.separator + fileName;
        File file = new File(path);
        if (!file.delete()) {
            throw new Exception("删除文件失败");
        }
        return WebResult.success("删除成功");
    }

    @RequestMapping("/file/download/{fileName}")
    public void downloadFile (@PathVariable String fileName, HttpServletResponse response) throws Exception {
        String path = this.ocrConfig.getStoragePath() + File.separator + fileName;
        // 读到流中
        InputStream inputStream = new FileInputStream(path);// 文件的存放路径
        response.reset();
        response.setContentType("application/octet-stream");
        String filename = new File(path).getName();
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
    }

    @RequestMapping("/file/list")
    public WebResult listFile () {
        File storageDir = new File(ocrConfig.getStoragePath());
        List<Map<String, Object>> files = new ArrayList<>();
        if (storageDir.exists() && storageDir.isDirectory()) {
            File[] children = storageDir.listFiles();
            if (children != null) {
                files = Arrays.stream(children)
                        .sorted(Comparator.comparing(File::lastModified))
                        .map(file -> new HashMap<String, Object>(){{
                            put("name", file.getName());
                            put("size", file.length());
                            put("path", "/ocr/file/download/" + file.getName());
                            put("modify_time", DateUtils.getString(file.lastModified(), DateUtils.DATETIME_FORMAT));
                        }})
                        .collect(Collectors.toList());
            }
        }
        return WebResult.success(files);
    }

    @GetMapping("/type/list")
    public WebResult listOcrType() {
        return WebResult.success(Arrays.stream(BaiduOcrType.values()).map(item -> new HashMap<String, Object>(){{
            put("id", item.getValue());
            put("label", item.getLabel());
        }}).collect(Collectors.toList()));
    }

    @RequestMapping("/recognition")
    public WebResult recognition(@RequestParam(name = "file_name") String fileName, @RequestParam(name = "ocr_type") BaiduOcrType ocrType) throws Exception {
        // 获取文件
        String path = this.ocrConfig.getStoragePath() + File.separator + fileName;

        AipOcr client = this.getAipOcrClient();

        HashMap<String, String> options = new HashMap<>();
        HashMap<String, Object> optionsAlias = new HashMap<>();

        JSONObject res;
        if (BaiduOcrType.ACCURATE.equals(ocrType)) {
            res = client.accurateGeneral(path, options);
        } else if (BaiduOcrType.ACCURATE_BASIC.equals(ocrType)) {
            res = client.basicAccurateGeneral(path, options);
        } else if (BaiduOcrType.GENERAL_BASIC.equals(ocrType)) {
            res = client.basicGeneral(path, options);
        } else if (BaiduOcrType.GENERAL.equals(ocrType)) {
            res = client.general(path, options);
        } else if (BaiduOcrType.DOC_ANALYSIS_OFFICE.equals(ocrType)) {
            res = client.docAnalysisOffice(path, optionsAlias);
        } else if (BaiduOcrType.WEB_IMAGE.equals(ocrType)) {
            res = client.webImage(path, options);
        } else if (BaiduOcrType.WEB_IMAGE_LOC.equals(ocrType)) {
            res = client.webimageLoc(path, options);
        } else if (BaiduOcrType.HANDWRITING.equals(ocrType)) {
            res = client.handwriting(path, options);
        } else if (BaiduOcrType.TABLE.equals(ocrType)) {
            res = client.form(path, options);
        } else if (BaiduOcrType.FORM_ASYNC.equals(ocrType)) {
            res = client.tableRecognitionAsync(path, options);
        } else if (BaiduOcrType.FORM.equals(ocrType)) {
            res = client.tableRecognizeToJson(path, 2000);
        } else if (BaiduOcrType.SEAL.equals(ocrType)) {
            res = client.seal(path);
        } else if (BaiduOcrType.NUMBERS.equals(ocrType)) {
            res = client.numbers(path, options);
        } else if (BaiduOcrType.QRCODE.equals(ocrType)) {
            res = client.qrcode(path, options);
        } else if (BaiduOcrType.CUSTOM.equals(ocrType)) {
            options.put("templateSign", "fhcql");
            res = client.custom(path, options);
        } else {
            throw new Exception("不支持的识别类型");
        }

        return WebResult.success(res.toString(2));
    }

    private AipOcr getAipOcrClient() {
        if (this.baiduOcrClient == null) {
            this.baiduOcrClient = new AipOcr(this.ocrConfig.getBaiduAppId(), this.ocrConfig.getBaiduApiKey(), this.ocrConfig.getBaiduSecretKey());
        }
        return this.baiduOcrClient;
    }
}

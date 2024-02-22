package com.example.controller;

import com.example.common.CustomException;
import com.example.common.R;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
//    @Value("${Reggie.path}");
    @PostMapping("/upload")
    public R upload(MultipartFile file) throws IOException {
        String uploadPath = "/Users/kong/Desktop/";
        File dir = new File(uploadPath);
        if(!dir.exists()) dir.mkdirs();
        //File是一个临时文件，秀要转存到指定位置，否则本次请求完成后份时文件会删除
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString()+suffix;
        //使用uuid给file起名字，防止名字重复。
        try {
            file.transferTo(new File(uploadPath+fileName));
        }
        catch(CustomException exception){
            return R.error(String.valueOf(exception));
        }
        return R.success("上传成功");
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response,String name) throws Exception {
        String uploadPath = "/Users/kong/Desktop/";
        //通过输入流读取数据
        try{
            FileInputStream inputStream = new FileInputStream(new File(uploadPath + name));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len  = inputStream.read(bytes);
            response.setContentType("image/jpge");
            while(len!=-1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            inputStream.close();
            outputStream.close();
        }
        catch(Exception exception){
            exception.printStackTrace();
        }
    }


}

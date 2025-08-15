package telran.java57.farmmarket.service;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl {
    public String upload(MultipartFile file, String fileName) throws Exception {
        byte[] bytes = file.getBytes();
        FileCreateRequest req = new FileCreateRequest(bytes, (fileName == null || fileName.isBlank())
                ? file.getOriginalFilename() : fileName);
        req.setUseUniqueFileName(true);
        Result res = ImageKit.getInstance().upload(req);
        return res.getUrl();
    }
}
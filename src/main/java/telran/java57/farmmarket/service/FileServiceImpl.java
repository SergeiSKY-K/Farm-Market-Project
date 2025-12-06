package telran.java57.farmmarket.service;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import telran.java57.farmmarket.dto.FileUploadResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Set;

@Service
public class FileServiceImpl {

    // Разрешённые MIME-типы
    private static final Set<String> ALLOWED_MIME = Set.of(
            "image/jpeg", "image/png", "image/webp"
    );

    // Ограничение по пикселям (пример)
    private static final int MAX_WIDTH = 4000;
    private static final int MAX_HEIGHT = 4000;

    public FileUploadResponse uploadValidated(MultipartFile file, String fileName) throws Exception {
        // 1) MIME с клиента
        String clientMime = file.getContentType();

        // 2) «Магия» по содержимому (без внешних либ)
        // а) попробуем ImageIO — если это картинка, вернёт не-null
        BufferedImage img = ImageIO.read(file.getInputStream());
        if (img == null) {
            return FileUploadResponse.error("Unsupported or corrupted image");
        }

        // 3) Размеры изображения
        if (img.getWidth() > MAX_WIDTH || img.getHeight() > MAX_HEIGHT) {
            return FileUploadResponse.error("Image dimensions too large");
        }

        // 4) Белый список MIME (простая проверка)
        if (clientMime == null || ALLOWED_MIME.stream().noneMatch(clientMime::equalsIgnoreCase)) {
            // можно «доверять» формату по расширению / либо попытаться определить тип по ImageIO
            // но надёжнее держать строгий список
            return FileUploadResponse.error("Only JPEG/PNG/WEBP allowed");
        }

        // 5) Санитайз имени (если задано)
        String safeName = (fileName == null || fileName.isBlank())
                ? sanitize(file.getOriginalFilename())
                : sanitize(fileName);

        // 6) Upload в ImageKit
        byte[] bytes = file.getBytes();
        FileCreateRequest req = new FileCreateRequest(bytes, safeName);
        req.setUseUniqueFileName(true);
        Result res = ImageKit.getInstance().upload(req);

        // 7) Сохраняй и fileId и url (fileId нужен для удаления)
        return FileUploadResponse.ok(res.getUrl(), res.getFileId());
    }

    private String sanitize(String name) {
        if (name == null) return "image";
        // убираем путь и запрещённые символы
        String base = name.replace("\\", "/");
        base = base.substring(base.lastIndexOf('/') + 1);
        base = base.replaceAll("[\\r\\n\\t]", "_")
                .replaceAll("[^A-Za-z0-9._-]", "_");
        if (base.isBlank()) base = "image";
        return base;
    }
}




//@Service
//public class FileServiceImpl {
//    public String upload(MultipartFile file, String fileName) throws Exception {
//        byte[] bytes = file.getBytes();
//        FileCreateRequest req = new FileCreateRequest(bytes, (fileName == null || fileName.isBlank())
//                ? file.getOriginalFilename() : fileName);
//        req.setUseUniqueFileName(true);
//        Result res = ImageKit.getInstance().upload(req);
//        return res.getUrl();
//    }
//}
package com.booktree.booktreespring.Controller;

import com.booktree.booktreespring.Domain.Book;
import com.booktree.booktreespring.Domain.Dto.BookDto;
import com.booktree.booktreespring.Domain.Dto.TreeDto;
import com.booktree.booktreespring.Domain.Tree;
import com.booktree.booktreespring.Repository.BookRepository;
import com.booktree.booktreespring.Repository.TreeRepository;
import com.booktree.booktreespring.Service.FileService;
import com.booktree.booktreespring.Service.FireBaseService;
import com.booktree.booktreespring.Util.BasicResponse;
import com.booktree.booktreespring.Util.CommonResponse;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class TreeController {

    private final TreeRepository treeRepository;
    private final FileService fileService;
    private final BookRepository bookRepository;
    private final FireBaseService fireBaseService;

    @Autowired
    public TreeController(TreeRepository treeRepository, FileService fileService, BookRepository bookRepository, FireBaseService fireBaseService) {
        this.treeRepository = treeRepository;
        this.fileService = fileService;
        this.bookRepository = bookRepository;
        this.fireBaseService = fireBaseService;
    }

    String filePath = "C:\\Users\\choi\\Desktop\\booktree\\booktree-spring\\src\\main\\java\\com\\booktree\\booktreespring\\Image\\";
    /**
     * 내 트리 정보 가져오기
     */
    @GetMapping("/tree")
    @ResponseBody
    public Tree getTreeController(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();

        Optional<Tree> tree = treeRepository.findByUserName(username);
        Tree result = tree.get();
        return result;
    }

    /**
     * 트리가 소유한 책 목록 반환
     **/
    @GetMapping("/tree/decoration")
    @ResponseBody
    public List<Book> getDecorationController(String ownerName){
        List<Book> findBook = bookRepository.findByOwnerName(ownerName);
        return findBook;
    }


    /**
     * 파이어베이스 이용 api
     */
    @PostMapping("/files")
    @ResponseBody
    public String uploadFile(@RequestBody MultipartFile file) throws IOException, FirebaseAuthException {
        if(file.isEmpty()){
            return "is empty";
        }
        return fireBaseService.uploadFiles(file);
    }


    /**
     *  트리 장식(책)의 이미지 반환
     */
    @GetMapping("/tree/decoration/image")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<? extends BasicResponse> getDecorationImageController(@RequestBody String filename) throws IOException {
        System.out.println("찾는 파일의 이름: " + filename);

        Resource resource1 = new FileSystemResource(filePath + filename);
        return ResponseEntity.ok().body(new CommonResponse<>(resource1));
        // return ResponseEntity.ok().body(new CommonResponse<>(true));
        //return ResponseEntity.ok().body(new CommonResponse<>(fileService.getFile(filename)));
    }

//    @GetMapping("/tree/decoration/image")
//    @ResponseBody
//    public MultipartFile processImg(@RequestBody String filename) throws IOException {
//        UrlResource urlResource = null;
//        try {
//            urlResource = new UrlResource("file:" + createPath(filename));
//        }catch(MalformedURLException e){
//            e.printStackTrace();
//        }
//        File file = urlResource.getFile();
//        System.out.println(file.toString());
//        FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
//        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
//        return multipartFile;
//    }
//    // 파일 경로 구성
//    public String createPath(String storeFilename) {
//        return filePath + storeFilename;
//    }
//
//    /**
//     *  다른 사람 트리 가져오기
//     */
//    @GetMapping("/tree/other")

//    public Tree getOtherTreeController(){
//
//    }

    /**
     * 최초 트리 생성하기
     */
    @PostMapping("/tree")
    @ResponseBody
    public Tree makeTreeController(@RequestBody TreeDto treeDto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();

        Tree tree = Tree.builder()
                .userName(username)
                .title(treeDto.getTitle())
                .explanation(treeDto.getExplanation())
                .build();

        return treeRepository.save(tree);
    }

//    /**
//     * 트리 기본 정보 수정하기
//     */
//    @PutMapping("/tree")
//    public Tree updateTreeController(){
//
//    }

    /**
     * 트리에 장식 달기
     */
    @PostMapping("/tree/book")
    @ResponseBody
    public Book addBookController(@ModelAttribute BookDto bookDto){
        String filename = fileService.uploadFile(bookDto.getFile());

        Book newBook = Book.builder()
                .ownerName(bookDto.getOwnerName())
                .title(bookDto.getTitle())
                .content(bookDto.getContent())
                .score(bookDto.getScore())
                .filename(filename)
                .build();

        return bookRepository.save(newBook);
    }

//
//    /**
//     * 트리에서 장식 삭제하기
//     */
//    @DeleteMapping("/tree/book")
//    public Tree deleteBookController(){
//
//    }
}

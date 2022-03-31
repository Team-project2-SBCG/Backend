package com.booktree.booktreespring.Controller;

import com.booktree.booktreespring.Domain.Book;
import com.booktree.booktreespring.Domain.Dto.BookDto;
import com.booktree.booktreespring.Domain.Dto.TreeDto;
import com.booktree.booktreespring.Domain.Tree;
import com.booktree.booktreespring.Repository.BookRepository;
import com.booktree.booktreespring.Repository.TreeRepository;
import com.booktree.booktreespring.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@Controller
public class TreeController {

    private final TreeRepository treeRepository;
    private final FileService fileService;
    private final BookRepository bookRepository;

    @Autowired
    public TreeController(TreeRepository treeRepository, FileService fileService, BookRepository bookRepository) {
        this.treeRepository = treeRepository;
        this.fileService = fileService;
        this.bookRepository = bookRepository;
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

//    /**
//     *  책의 이미지 반환
//     */
//    @GetMapping("/tree/decoration/image")
//    @ResponseBody
//    public MultipartFile getDecorationImageController(@RequestBody String filename) throws IOException {
//        System.out.println("찾는 파일의 이름: " + filename);
//        return fileService.getFile(filename);
////
////        MultipartFile multipartFile = new MockMultipartFile(filename, new FileInputStream(new File(filePath + filename)));
////        return multipartFile;
//    }

    @GetMapping("/tree/decoration/image")
    @ResponseBody
    public Resource processImg(@RequestBody String filename) throws IOException {
        UrlResource urlResource = null;
        try {
            urlResource = new UrlResource("file:" + createPath(filename));
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return urlResource;
    }
    // 파일 경로 구성
    public String createPath(String storeFilename) {
        return filePath + storeFilename;
    }
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

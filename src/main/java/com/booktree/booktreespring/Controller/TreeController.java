package com.booktree.booktreespring.Controller;

import com.booktree.booktreespring.Domain.Book;
import com.booktree.booktreespring.Domain.Dto.BookDto;
import com.booktree.booktreespring.Domain.Dto.TreeDto;
import com.booktree.booktreespring.Domain.Dto.TreeUpdateDto;
import com.booktree.booktreespring.Domain.Tree;
import com.booktree.booktreespring.Repository.BookRepository;
import com.booktree.booktreespring.Repository.TreeRepository;
import com.booktree.booktreespring.Repository.TreeRepository2;
import com.booktree.booktreespring.Service.FileService;
import com.booktree.booktreespring.Service.FireBaseService;
import com.booktree.booktreespring.Util.BasicResponse;
import com.booktree.booktreespring.Util.CommonResponse;
import com.booktree.booktreespring.Util.ErrorResponse;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class TreeController {

    private final TreeRepository treeRepository;
    private final TreeRepository2 treeRepository2;
    private final FileService fileService;
    private final BookRepository bookRepository;
    private final FireBaseService fireBaseService;

    @Autowired
    public TreeController(TreeRepository treeRepository, TreeRepository2 treeRepository2, FileService fileService, BookRepository bookRepository, FireBaseService fireBaseService) {
        this.treeRepository = treeRepository;
        this.treeRepository2 = treeRepository2;
        this.fileService = fileService;
        this.bookRepository = bookRepository;
        this.fireBaseService = fireBaseService;
    }

    /**
     * 내 트리 정보 가져오기
     */
    @GetMapping("/my-tree")
    @ResponseBody
    public Tree getTreeController(){
        System.out.println("내 트리 정보를 가져옵니다.");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();
        // 여기 트리 없을 경우 에러처리 해줘야함.
        Optional<Tree> tree = treeRepository.findByUserName(username);
        return tree.get();
    }

    /**
     * 트리가 소유한 책 목록 반환
     **/
    @GetMapping("/tree/books")
    @ResponseBody
    public List<Book> getDecorationController(@RequestBody String ownerName){
        System.out.println("트리가 소유한 책 목록 요청입니다.");
        List<Book> findBook = bookRepository.findByOwnerName(ownerName);
        return findBook;
    }

    /**
     * 최초 트리 생성하기 - 구현 완료.
     */
    @PostMapping("/tree")
    @ResponseBody
    public Tree makeTreeController(@RequestBody TreeDto treeDto){
        System.out.println("최초 트리 생성 요청입니다.");
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

    /**
     * 트리 기본 정보 수정하기 - 구현 완료.
     */
    @PutMapping("/tree")
    @ResponseBody
    public ResponseEntity<? extends BasicResponse> updateTreeController(@RequestBody TreeUpdateDto treeUpdateDto){
        System.out.println("트리 기본 정보 수정 요청입니다.");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();

        if (treeUpdateDto.getOwnerName().equals(username)){ // 자신의 트리가 맞다면
                Optional<Tree> findTree = treeRepository.findByUserName(treeUpdateDto.getOwnerName());
                findTree.get().setTitle(treeUpdateDto.getTitle());
                findTree.get().setExplanation(treeUpdateDto.getExplanation());
                Tree resultTree = treeRepository.save(findTree.get());
            return ResponseEntity.ok().body(new CommonResponse<>(resultTree));
        }else{
            return ResponseEntity.ok().body(new ErrorResponse("자신의 트리만 업데이트할 수 있습니다."));
        }
    }

    /**
     * 트리에 장식 달기 - 구현 완료.
     */
    @PostMapping("/tree/book")
    @ResponseBody
    public Book addBookController(@ModelAttribute BookDto bookDto) throws IOException, FirebaseAuthException {
        System.out.println("트리에 장식 달기 요청입니다.");
        MultipartFile multipartFile = bookDto.getFile();
        String filename = UUID.randomUUID().toString() + ".jpg";
        String url = fireBaseService.uploadFiles(multipartFile, filename);

        Book newBook = Book.builder()
                .ownerName(bookDto.getOwnerName())
                .title(bookDto.getTitle())
                .content(bookDto.getContent())
                .score(bookDto.getScore())
                .filename(filename)
                .firebaseUrl(url)
                .build();

        return bookRepository.save(newBook);
    }


    /**
     * 트리 좋아요 기능 - 구현 완료
     */
    @PostMapping("/tree/like")
    @ResponseBody
    public Tree treeLikeController(@RequestBody String ownerName){ // @RequestBody 이거 객체 받는걸로 바꾸자.
        System.out.println("트리 좋아요 요청입니다.");
        Optional<Tree> findTree = treeRepository.findByUserName(ownerName);
        Tree tree = findTree.get();
        tree.setUpCnt(tree.getUpCnt() + 1);
        return treeRepository.save(tree);
    }

    /**
     * 장식 삭제하기
     */
    @DeleteMapping("/tree/book")
    @ResponseBody
    public boolean deleteBookController(@RequestBody Long bookId){
        System.out.println("트리 장식 삭제 요청입니다.");
        Optional<Book> findBook = bookRepository.findById(bookId);
        String fileName= findBook.get().getFilename();
        bookRepository.deleteById(bookId);
        return fireBaseService.deleteFiles(fileName);
    }

    /**
     * 트리 삭제하기
     */
    @DeleteMapping("/tree")
    @ResponseBody
    public int deleteTreeController(@RequestBody Long treeId){
        Tree findTree = treeRepository2.findById(treeId);
        List<Book> findBooks = bookRepository.findByOwnerName(findTree.getUserName());
        for(Book book: findBooks){
            fireBaseService.deleteFiles(book.getFilename()); // 파이어베이스에서 이미지 삭제하기
            bookRepository.deleteById(book.getId());
        }
        return treeRepository2.deleteById(treeId);
    }
}

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
     * ??? ?????? ?????? ????????????
     */
    @GetMapping("/my-tree")
    @ResponseBody
    public Tree getTreeController(){
        System.out.println("??? ?????? ????????? ???????????????.");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();
        // ?????? ?????? ?????? ?????? ???????????? ????????????.
        Optional<Tree> tree = treeRepository.findByUserName(username);
        return tree.get();
    }

    /**
     * ????????? ????????? ??? ?????? ??????
     **/
    @GetMapping("/tree/books")
    @ResponseBody
    public List<Book> getDecorationController(@RequestBody String ownerName){
        System.out.println("????????? ????????? ??? ?????? ???????????????.");
        List<Book> findBook = bookRepository.findByOwnerName(ownerName);
        return findBook;
    }

    /**
     * ?????? ?????? ???????????? - ?????? ??????.
     */
    @PostMapping("/tree")
    @ResponseBody
    public Tree makeTreeController(@RequestBody TreeDto treeDto){
        System.out.println("?????? ?????? ?????? ???????????????.");
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
     * ?????? ?????? ?????? ???????????? - ?????? ??????.
     */
    @PutMapping("/tree")
    @ResponseBody
    public ResponseEntity<? extends BasicResponse> updateTreeController(@RequestBody TreeUpdateDto treeUpdateDto){
        System.out.println("?????? ?????? ?????? ?????? ???????????????.");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();

        if (treeUpdateDto.getOwnerName().equals(username)){ // ????????? ????????? ?????????
                Optional<Tree> findTree = treeRepository.findByUserName(treeUpdateDto.getOwnerName());
                findTree.get().setTitle(treeUpdateDto.getTitle());
                findTree.get().setExplanation(treeUpdateDto.getExplanation());
                Tree resultTree = treeRepository.save(findTree.get());
            return ResponseEntity.ok().body(new CommonResponse<>(resultTree));
        }else{
            return ResponseEntity.ok().body(new ErrorResponse("????????? ????????? ??????????????? ??? ????????????."));
        }
    }

    /**
     * ????????? ?????? ?????? - ?????? ??????.
     */
    @PostMapping("/tree/book")
    @ResponseBody
    public Book addBookController(@ModelAttribute BookDto bookDto) throws IOException, FirebaseAuthException {
        System.out.println("????????? ?????? ?????? ???????????????.");
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
     * ?????? ????????? ?????? - ?????? ??????
     */
    @PostMapping("/tree/like")
    @ResponseBody
    public Tree treeLikeController(@RequestBody String ownerName){ // @RequestBody ?????? ?????? ???????????? ?????????.
        System.out.println("?????? ????????? ???????????????.");
        Optional<Tree> findTree = treeRepository.findByUserName(ownerName);
        Tree tree = findTree.get();
        tree.setUpCnt(tree.getUpCnt() + 1);
        return treeRepository.save(tree);
    }

    /**
     * ?????? ????????????
     */
    @DeleteMapping("/tree/book")
    @ResponseBody
    public boolean deleteBookController(@RequestBody Long bookId){
        System.out.println("?????? ?????? ?????? ???????????????.");
        Optional<Book> findBook = bookRepository.findById(bookId);
        String fileName= findBook.get().getFilename();
        bookRepository.deleteById(bookId);
        return fireBaseService.deleteFiles(fileName);
    }

    /**
     * ?????? ????????????
     */
    @DeleteMapping("/tree")
    @ResponseBody
    public int deleteTreeController(@RequestBody Long treeId){
        Tree findTree = treeRepository2.findById(treeId);
        List<Book> findBooks = bookRepository.findByOwnerName(findTree.getUserName());
        for(Book book: findBooks){
            fireBaseService.deleteFiles(book.getFilename()); // ???????????????????????? ????????? ????????????
            bookRepository.deleteById(book.getId());
        }
        return treeRepository2.deleteById(treeId);
    }
}

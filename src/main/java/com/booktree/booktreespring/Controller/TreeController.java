package com.booktree.booktreespring.Controller;

import com.booktree.booktreespring.Domain.Dto.TreeDto;
import com.booktree.booktreespring.Domain.Tree;
import com.booktree.booktreespring.Repository.TreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TreeController {

    private final TreeRepository treeRepository;

    @Autowired
    public TreeController(TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
    }

//    /**
//     * 내 트리 정보 가져오기
//     */
//    @GetMapping("/tree")
//    public Tree getTreeController(){
//
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
    public Tree makeTreeController(@RequestBody TreeDto treeDto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();

        Tree tree = Tree.builder()
                .username(username)
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

//    /**
//     * 트리에 장식 달기
//     */
//    @PostMapping("/tree/book")
//    public Tree addBookController(){
//
//    }
//
//    /**
//     * 트리에서 장식 삭제하기
//     */
//    @DeleteMapping("/tree/book")
//    public Tree deleteBookController(){
//
//    }
}

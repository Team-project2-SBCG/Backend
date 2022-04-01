package com.booktree.booktreespring.Repository;

import com.booktree.booktreespring.Domain.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class TreeRepository2 {
    private final EntityManager em;

    @Autowired
    public TreeRepository2(EntityManager em) {
        this.em = em;
    }
//
//    public Tree findByUserName(String ownerName){
//        String sql = "select t from Tree t where t.userName = :ownerName";
//        return em.createQuery(sql, Tree.class)
//                .setParameter("ownerName", ownerName)
//                .getSingleResult();
//    }

    public List<Tree> findByUserName(String ownerName){
        String sql = "select t from Tree t where t.userName = :ownerName";
        return em.createQuery(sql, Tree.class).setParameter("ownerName", ownerName).getResultList();
    }

    public Tree findById(Long id) {
        String sql = "select t from Tree t where t.id = :id";
        return em.createQuery(sql, Tree.class).setParameter("id",id).getSingleResult();
    }
}

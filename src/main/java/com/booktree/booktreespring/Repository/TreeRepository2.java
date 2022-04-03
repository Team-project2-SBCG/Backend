package com.booktree.booktreespring.Repository;

import com.booktree.booktreespring.Domain.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TreeRepository2 {
    private final EntityManager em;

    @Autowired
    public TreeRepository2(EntityManager em) {
        this.em = em;
    }

    public List<Tree> findByUserName(String ownerName){
        String sql = "select t from Tree t where t.userName = :ownerName";
        return em.createQuery(sql, Tree.class).setParameter("ownerName", ownerName).getResultList();
    }

    public Tree findById(Long id) {
        String sql = "select t from Tree t where t.id = :id";
        return em.createQuery(sql, Tree.class).setParameter("id",id).getSingleResult();
    }

    @Transactional
    public int deleteById(Long id){
        String sql = "delete from Tree t where t.id = :id";
        Query query = em.createQuery(sql).setParameter("id", id);
        return query.executeUpdate();
    }
}

package com.twk.thymeleafprac.service.board;

import com.twk.thymeleafprac.domain.BoardParam;
import com.twk.thymeleafprac.domain.CmtParam;
import com.twk.thymeleafprac.domain.FavParam;
import com.twk.thymeleafprac.domain.PageParam;
import com.twk.thymeleafprac.mapper.BoardMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;

@Service
@Log
public class BoardServiceImpl implements BoardService{
    @Autowired
    BoardMapper boardMapper;

    @Override
    public List<HashMap<String, Object>> getBoardList(PageParam param){
        List<HashMap<String,Object>> result = boardMapper.getBoardList(param);
        return result;
    }

    @Override
    public int getTotalCnt(){
        return boardMapper.getTotalCnt();
    }

    @Override
    public BoardParam selectBoard(String key){
        return boardMapper.selectBoard(key);
    }

    @Override
    public List<CmtParam> getCmtList(String key) {
        return boardMapper.getCmtList(key);
    }

    @Override
    public CmtParam getCmt(String cmtKey) { return boardMapper.getCmt(cmtKey); }

    @Override
    public int getLikeCnt(String key) { return boardMapper.getLikeCnt(key); }

    @Override
    public int getCmtCheck(FavParam param) { return boardMapper.getCmtCheck(param); }

    @Override
    public int favoriteBoardOn(FavParam param) {
        return boardMapper.favoriteBoardOn(param);
    }

    @Override
    public int favoriteBoardOff(FavParam param) {
        return boardMapper.favoriteBoardOff(param);
    }

    @Override
    public void insertCmt(CmtParam param){
        boardMapper.insertCmt(param);
    }

    @Override
    public void insertBoard(BoardParam param) { boardMapper.insertBoard(param);}

    @Override
    public List<BoardParam> getTaskContent(String key) { return boardMapper.getTaskContent(key); }

    @Override
    public int nextval() { return boardMapper.nextval(); }

    @Override
    public int nextvalCmt() { return boardMapper.nextvalCmt(); }

    @Override
    public int deleteTaskCmt(String taskKey){
        return boardMapper.deleteTaskCmt(taskKey);
    }

    @Override
    public int deleteTaskBoard(String taskKey){
        return boardMapper.deleteTaskBoard(taskKey);
    }

    @Override
    public int deleteBoard(String boardKey){ return boardMapper.deleteBoard(boardKey); }

    @Override
    public int deleteCmt(String cmtKey){ return boardMapper.deleteCmt(cmtKey); }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public int updateBoard(BoardParam param) {
        try {
            int result = boardMapper.updateBoard(param);

            if ( result != 1 ) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return 99;
            } else {
                return result;
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return 99;
    }

    @Override
    public int updateCmt(CmtParam param) {
        try {
            int result = boardMapper.updateCmt(param);

            if ( result != 1 ) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return 99;
            } else {
                return result;
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return 99;
    }

}

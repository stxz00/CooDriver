package com.twk.thymeleafprac.service.board;

import com.twk.thymeleafprac.domain.BoardParam;
import com.twk.thymeleafprac.domain.CmtParam;
import com.twk.thymeleafprac.domain.FavParam;
import com.twk.thymeleafprac.domain.PageParam;

import java.util.HashMap;
import java.util.List;

public interface BoardService {
    public List<HashMap<String, Object>> getBoardList(PageParam param);
    public List<BoardParam> getTaskContent(String key);
    public int getTotalCnt();
    public BoardParam selectBoard(String key);
    public List<CmtParam> getCmtList(String key);
    public CmtParam getCmt(String cmtKey);
    public int getLikeCnt(String key);
    public int getCmtCheck(FavParam param);
    public int favoriteBoardOn(FavParam param);
    public int favoriteBoardOff(FavParam param);
    public void insertCmt(CmtParam param);
    public void insertBoard(BoardParam param);
    public int nextval();
    public int nextvalCmt();
    public int deleteTaskCmt(String taskKey);
    public int deleteTaskBoard(String taskKey);
    public int deleteBoard(String boardKey);
    public int deleteCmt(String cmtKey);
    int updateBoard(BoardParam param);
    int updateCmt(CmtParam param);

}

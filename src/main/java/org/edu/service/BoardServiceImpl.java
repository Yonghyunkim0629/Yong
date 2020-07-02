package org.edu.service;

import java.util.List;

import javax.inject.Inject;

import org.edu.dao.IF_BoardDAO;
import org.edu.vo.BoardVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardServiceImpl implements IF_BoardService{
   
   @Inject
   private IF_BoardDAO boardDAO;
   
   @Transactional
   @Override
   public void insertBoard(BoardVO boardVO) throws Exception {
      boardDAO.insertBoard(boardVO);
      //첨부파일용 서비스 추가
      String[] files = boardVO.getFiles();
      if(files == null) {return; }
      for(String fileName : files) {
         boardDAO.insertAttach(fileName);
      }
   }

   @Override
   public List<BoardVO> selectBoard() throws Exception {
      
      return boardDAO.selectBoard();
   }

   @Override
   public void updateBoard(BoardVO boardVO) throws Exception {
      boardDAO.updateBoard(boardVO);
      
   }

   @Override
   public void deleteBoard(Integer bno) throws Exception {
      boardDAO.deleteBoard(bno);
   }

   @Override
   public BoardVO viewBoard(Integer bno) throws Exception {  
      return boardDAO.viewBoard(bno);
   }

@Override
public String selectAttach(Integer bno) throws Exception {
	return boardDAO.selectAttach(bno);
}

 
   //private String name = new String("홍길동");
   //간단하게 나타내는것 처럼 할 수 있다. 
   /*옛날 방식
    * public class Assembler {
    public Assembler() {
        memberDao = new MemberDao();
        regSvc = new MemberRegisterService(memberDao);
        pwdSvc = new ChangePasswordService();
        pwdSvc.setMemberDao(memberDao);
    }
}
    */
   
}
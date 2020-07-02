package org.edu.controller;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.edu.service.IF_BoardService;
import org.edu.service.IF_MemberService;
import org.edu.vo.BoardVO;
import org.edu.vo.MemberVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {
   
   @Inject
   private IF_BoardService boardService;
   
   @Inject
   private IF_MemberService memberService;
   //첨부파일 업로드 경로 변수값으로 가져옴 servlet-context
   @Resource(name="uploadPath")
   private String uploadPath;
   
   /**
    * 게시물관리 리스트 입니다.
    * @throws Exception 
    */
   @RequestMapping(value = "/admin/board/list", method = RequestMethod.GET)
   public String boardList(Locale locale, Model model) throws Exception {
      List<BoardVO> list = boardService.selectBoard();
      //모델클래스로 jsp화면으로 boardService에서 셀렉트한 list값을 boardList변수명으로 보낸다.
      //model { list -> boardList -> jsp }
      model.addAttribute("boardList", list);
      return "admin/board/board_list";
   }
   /**
    * 게시물관리 상세보기 입니다.
    * @throws Exception 
    */
   @RequestMapping(value = "/admin/board/view", method = RequestMethod.GET)
   public String boardView(@RequestParam("bno") Integer bno,Locale locale, Model model) throws Exception {
      BoardVO boardVO = boardService.viewBoard(bno);
      //여기서 부터 첨부파일 출력물
     String files = boardService.selectAttach(bno);
      /*String[] filenames = {};
      for(String fileName : files
    		  ) {
    	  filenames = new String[] {fileName};
      }     */
     //여러개 파일에서 1개 파일만 받는 것으로 변경
     String[] filenames = new String[] {files}; 
     boardVO.setFiles(filenames);//string[]
      //여기까지 첨부파일 때문에 추가
      model.addAttribute("boardVO", boardVO);
      return "admin/board/board_view";
   }
   
   /**
    * 게시물관리 > 등록 입니다.
    * @throws Exception 
    */
   @RequestMapping(value = "/admin/board/write", method = RequestMethod.GET)
   public String boardWrite(Locale locale, Model model) throws Exception {
      
      return "admin/board/board_write";
   }
   @RequestMapping(value = "/admin/board/write", method = RequestMethod.POST)
   public String boardWrite(MultipartFile file,BoardVO boardVO,Locale locale, RedirectAttributes rdat) throws Exception {
      String originalName = file.getOriginalFilename(); //jsp에서 전송받은 파일의 이름
      UUID uid = UUID.randomUUID(); //랜덤문자 구하기
      String saveName = uid.toString() + "." + originalName.split("\\.")[1]; // 한글 파일명 처리 때문임.
      String[] files = new String[] {saveName}; //형변환 get set을 쓰려고(배열로 선언해서 스트링으로 형변환)
      boardVO.setFiles(files); //셋으로 파일을 저장했기때문에 보낼 수 있음.
        
        //위는 DB에 첨부파일명을 저장하기까지 여정 
        //이 아래부터는 실제파일을 폴더에 저장하기 시작
        byte[] fileData = file.getBytes(); //67번째줄 보드롸이트 파일(매개변수) 받음
         File target = new File(uploadPath, saveName);//31~33 업로드 경로 변수값으로 받음 저장 될 위치 지정
         FileCopyUtils.copy(fileData,target);
         
         boardService.insertBoard(boardVO);
       rdat.addFlashAttribute("msg", "입력");
      return "redirect:/admin/board/list";
   }
   
   /**
    * 게시물관리 > 수정 입니다.
    * @throws Exception 
    */
   @RequestMapping(value = "/admin/board/update", method = RequestMethod.GET)
   public String boardUpdate(@RequestParam("bno") Integer bno, Locale locale, Model model) throws Exception {
      BoardVO boardVO = boardService.viewBoard(bno);
      model.addAttribute("boardVO", boardVO);
      return "admin/board/board_update";
   }
   @RequestMapping(value = "/admin/board/update", method = RequestMethod.POST)
   public String boardUpdate(BoardVO boardVO,Locale locale, RedirectAttributes rdat) throws Exception {
      boardService.updateBoard(boardVO);
      rdat.addFlashAttribute("msg", "수정");
      return "redirect:/admin/board/view?bno=" + boardVO.getBno();
   }
   
   /**
    * 게시물관리 > 삭제 입니다.
    * @throws Exception 
    */
   @RequestMapping(value = "/admin/board/delete", method = RequestMethod.POST)
   public String boardDelete(@RequestParam("bno") Integer bno, Locale locale,RedirectAttributes rdat) throws Exception {   
      boardService.deleteBoard(bno);
      rdat.addFlashAttribute("msg", "삭제");
      return "redirect:/admin/board/list";
   }
   
   /**
    * 회원관리 리스트 입니다.
    * @throws Exception 
    */
   @RequestMapping(value = "/admin/member/list", method = RequestMethod.GET)
   public String memberList(Locale locale, Model model) throws Exception {
      List<MemberVO> list = memberService.selectMember();
      //모델클래스로 jsp화면으로 memberService에서 셀렉트한 list값을 memberList변수명으로 보낸다.
      //model { list -> memberList -> jsp }
      model.addAttribute("memberList", list);
      return "admin/member/member_list";
   }
   
   /**
    * 회원관리 상세보기 입니다.
    * @throws Exception 
    */
   @RequestMapping(value = "/admin/member/view", method = RequestMethod.GET)
   public String memberView(@RequestParam("user_id") String user_id, Locale locale, Model model) throws Exception {
      MemberVO memberVO = memberService.viewMember(user_id);
      model.addAttribute("memberVO", memberVO);
      return "admin/member/member_view";
   }
   
   /**
    * 회원관리 > 등록 입니다.
    * @throws Exception 
    */
   @RequestMapping(value = "/admin/member/write", method = RequestMethod.GET)
   public String memberWrite(Locale locale, Model model) throws Exception {
      
      return "admin/member/member_write";
   }
   @RequestMapping(value = "/admin/member/write", method = RequestMethod.POST)
   public String memberWrite(MemberVO memberVO, Locale locale, RedirectAttributes rdat) throws Exception {
      memberService.insertMember(memberVO);
      rdat.addFlashAttribute("msg", "입력");
      return "redirect:/admin/member/list";
   }
   
   /**
    * 회원관리 > 수정 입니다.
    * @throws Exception 
    */
   @RequestMapping(value = "/admin/member/update", method = RequestMethod.GET)
   public String memberUpdate(@RequestParam("user_id") String user_id, Locale locale, Model model) throws Exception {
      MemberVO memberVO = memberService.viewMember(user_id);
      model.addAttribute("memberVO", memberVO);
      return "admin/member/member_update";
   }
   @RequestMapping(value = "/admin/member/update", method = RequestMethod.POST)
   public String memberUpdate(MemberVO memberVO, Locale locale, RedirectAttributes rdat) throws Exception {
      memberService.updateMember(memberVO);
      rdat.addFlashAttribute("msg", "수정");
      return "redirect:/admin/member/view?user_id=" + memberVO.getUser_id();
   }
   
   /**
    * 회원관리 > 삭제 입니다.
    * @throws Exception 
    */
   @RequestMapping(value = "/admin/member/delete", method = RequestMethod.POST)
   public String memberDelete(@RequestParam("user_id") String user_id, Locale locale,RedirectAttributes rdat) throws Exception {
      memberService.deleteMember(user_id);
      rdat.addFlashAttribute("msg","삭제");
      return "redirect:/admin/member/list";
   }
   
   /**
    * 관리자 홈 입니다.
    */
   @RequestMapping(value = "/admin", method = RequestMethod.GET)
   public String home(Locale locale, Model model) {
   return "admin/home";
   }
   
}
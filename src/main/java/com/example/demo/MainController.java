package com.example.demo;

import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/board")
public class MainController {

    private final BoardRepository boardRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public MainController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 리스트
    @GetMapping("/list")
    public Page<Board> getBoardList( @PageableDefault(page = 0,size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    // 상세
    @GetMapping("/{id}")
    public Optional<Board> getBoardItem(@PathVariable Long id){
        return boardRepository.findById(id);
    }

    // 생성
    @PostMapping("/create")
    public String createBoard(@RequestBody Board board){
        boardRepository.save(board);
        return "create Success";
    }

    //수정
    @Transactional
    @PatchMapping("/update/{id}")
    public String updateBoard(@PathVariable Long id,@RequestBody Board board){

        Optional<Board> optionalBoard = boardRepository.findById(id);

        if(optionalBoard.isPresent()){
            Board existingBoard = optionalBoard.get();

            if(board.getCategory()!=null){
                existingBoard.setCategory(board.getCategory());
            }
            if(board.getContent()!=null){
                existingBoard.setContent(board.getContent());
            }
            if(board.getTitle()!=null){
                existingBoard.setTitle(board.getTitle());
            }

            entityManager.merge(existingBoard);
            return "Update Success";
        }
        else{
            return "Update Failed";
        }

    }

    // 삭제
    @DeleteMapping("/{id}")
    public String deleteBoardItem(@PathVariable Long id){
        boardRepository.deleteById(id);
        return "delete Success";
    }


}


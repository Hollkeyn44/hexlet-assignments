package exercise.service;

import exercise.dto.*;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.mapper.BookMapper;
import exercise.repository.AuthorRepository;
import exercise.repository.BookRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    // BEGIN
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private BookRepository bookRepository;

    public List<AuthorDTO> getAll() {
        var author = authorRepository.findAll();
        var result = author.stream()
                .map(authorMapper::map)
                .toList();
        return result;
    }

    public AuthorDTO findById(Long id) {
        var author =  authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not found"));
        var authorDTO = authorMapper.map(author);
        return authorDTO;
    }

    public AuthorDTO create(AuthorCreateDTO authorData) {
        try {
            var author = authorMapper.map(authorData);
            authorRepository.save(author);
            var authorDto = authorMapper.map(author);
            return authorDto;
        } catch (Exception exception) {
            throw new ConstraintViolationException(null);
        }
    }

    public AuthorDTO update(AuthorUpdateDTO authorData, Long id) {
        var author =  authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
        authorMapper.update(authorData, author);
        authorRepository.save(author);
        return authorMapper.map(author);
    }

    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
    // END
}

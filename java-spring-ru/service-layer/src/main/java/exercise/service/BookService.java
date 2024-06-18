package exercise.service;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.BookMapper;
import exercise.repository.AuthorRepository;
import exercise.repository.BookRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    // BEGIN
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookMapper bookMapper;

    public List<BookDTO> getAll() {
        var books = bookRepository.findAll();
        var result = books.stream()
                .map(bookMapper::map)
                .toList();
        return result;
    }

    public BookDTO findById(Long id) {
        var book =  bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
        var author = authorRepository.findById(book.getAuthor().getId());
        var bookDto = bookMapper.map(book);
        bookDto.setAuthorFirstName(author.get().getFirstName());
        bookDto.setAuthorLastName(author.get().getLastName());
        return bookDto;
    }

    public BookDTO create(BookCreateDTO bookData) {
        try {
            var book = bookMapper.map(bookData);
            bookRepository.save(book);
            var bookDto = bookMapper.map(book);
            return bookDto;
        } catch (Exception exception) {
            throw new ConstraintViolationException(null);
        }
    }

    public BookDTO update(BookUpdateDTO bookData, Long id) {
        var book =  bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
        bookMapper.update(bookData, book);
        bookRepository.save(book);
        var author = authorRepository.findById(bookData.getAuthorId().get());
        author.ifPresent(value -> value.addBook(book));
        author.ifPresent(value -> authorRepository.save(value));
        return bookMapper.map(book);
    }

    public void delete(Long id) {
        var book =  bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
        bookRepository.deleteById(id);
        var author = book.getAuthor();
        author.removeBook(book);
        authorRepository.save(author);
    }
    // END
}

package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.Album;
import io.github.haebin827.hiphopreview.kr.domain.Review;
import io.github.haebin827.hiphopreview.kr.domain.User;
import io.github.haebin827.hiphopreview.kr.dto.AlbumDTO;
import io.github.haebin827.hiphopreview.kr.dto.ReviewDTO;
import io.github.haebin827.hiphopreview.kr.dto.UserDTO;
import io.github.haebin827.hiphopreview.kr.repository.AlbumRepository;
import io.github.haebin827.hiphopreview.kr.repository.ReviewRepository;
import io.github.haebin827.hiphopreview.kr.repository.UserRepository;
import io.github.haebin827.hiphopreview.kr.service.ReviewServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    /*@Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private ReviewDTO reviewDTO;
    private User user;
    private Album album;
    private Review review;

    @BeforeEach
    public void setUp() {
        // ReviewDTO, User, Album, Review 객체 초기화
        reviewDTO = ReviewDTO.builder()
                .user(UserDTO.builder().id(1).name("John Doe").nickname("jdoe").build())
                .albumId(1)
                .title("Great Album")
                .content("I loved this album!")
                .rating(5.0f)
                .build();

        user = User.builder().id(1).name("John Doe").nickname("jdoe").build();
        album = Album.builder().id(1).title("Test Album").rating(4.5f).build();
        review = Review.builder()
                .user(user)
                .album(album)
                .title("Great Album")
                .content("I loved this album!")
                .rating(5.0f)
                .build();

        log.info("Review info: " + review);
    }

    @Test
    public void testSaveReview() {
        // userId와 albumId로 User와 Album을 찾도록 Mock 설정
        when(userRepository.findById(reviewDTO.getUser().getId())).thenReturn(Optional.of(user));
        when(albumRepository.findById(reviewDTO.getAlbum().getId())).thenReturn(Optional.of(album));

        // ModelMapper가 ReviewDTO를 Review로 매핑하도록 설정
        when(modelMapper.map(reviewDTO, Review.class)).thenReturn(review);

        // ReviewRepository가 Review를 저장하고 반환하도록 설정
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // ModelMapper가 저장된 Review를 ReviewDTO로 매핑하도록 설정
        when(modelMapper.map(review, ReviewDTO.class)).thenReturn(reviewDTO);

        // 서비스 메서드 호출
        ReviewDTO result = reviewService.saveReview(reviewDTO);

        // 검증
        assertEquals(reviewDTO.getTitle(), result.getTitle());
        assertEquals(reviewDTO.getContent(), result.getContent());
        assertEquals(reviewDTO.getRating(), result.getRating());
    }*/
}

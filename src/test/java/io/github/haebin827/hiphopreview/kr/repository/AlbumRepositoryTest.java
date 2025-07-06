package io.github.haebin827.hiphopreview.kr.repository;

import io.github.haebin827.hiphopreview.kr.domain.Album;
import io.github.haebin827.hiphopreview.kr.domain.Artist;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepo;

    @Autowired
    private ArtistRepository artistRepo;

    @Test
    public void testSave() {
        Album album = Album.builder()
                .title("TEST")
                .rating(4.5f)
                .build();
        albumRepo.save(album);
    }

    @Test
    @Transactional
    public void testFindTop30ByOrderByRegdateDesc() {
        Pageable pageable = PageRequest.of(0, 30); // 30개 제한
        List<Album> top30 = albumRepo.findTop30NewestAlbums(pageable);
        top30.forEach(album -> log.info("- " + album));
    }

    @Test
    @Transactional
    public void testFindByTitleContainingIgnoreCase() {
        Page<Album> albums = albumRepo.findByTitleContainingIgnoreCase("man", PageRequest.of(0, 30));
        for(Album album : albums) {
            log.info("TEST- " + album);
        }
    }

    /*@Test
    public void testSaveAlbumWithArtist() {
        // 연관된 엔티티인 Artist 생성 및 저장
        Artist artist = Artist.builder()
                .name("Artist Name")
                .build();
        artistRepo.save(artist);

        // Album 엔티티 생성 및 Artist 설정
        Album album = Album.builder()
                .title("Album Title")
                .build();
        album.getArtists().add(artist);

        // Album 저장
        Album savedAlbum = albumRepo.save(album);

        // 검증
        assertNotNull(savedAlbum.getId());
        assertFalse(savedAlbum.getArtists().isEmpty());
        assertEquals("Artist Name", savedAlbum.getArtists().get(0).getName());
    }

    @Test
    @Transactional
    public void testReadOne() {
        Album album = albumRepo.findById(2).orElseThrow();
        Hibernate.initialize(album.getArtists());  // 컬렉션을 초기화
        log.info("album info: " + album);
    }*/

}

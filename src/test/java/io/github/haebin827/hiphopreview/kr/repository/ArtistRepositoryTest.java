package io.github.haebin827.hiphopreview.kr.repository;

import groovy.util.logging.Log4j2;
import io.github.haebin827.hiphopreview.kr.domain.Album;
import io.github.haebin827.hiphopreview.kr.domain.Artist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@SpringBootTest
@Log4j2
public class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    @Transactional
    public void testFindByUuid() {
        // Replace with a UUID that exists in your test data or database
        String testUuid = "b95ce3ff-3d05-4e87-9e01-c97b66af13d4";

        // Fetch artist by UUID
        Optional<Artist> optionalArtist = artistRepository.findByUuid(testUuid);

        // Check if artist is present
        assertTrue(optionalArtist.isPresent(), "Artist should be found with given UUID");

        Artist artist = optionalArtist.get();
        System.out.println("Artist Details:");
        System.out.println("Name: " + artist.getName());
        System.out.println("Gender: " + artist.getGender());
        System.out.println("Type: " + artist.getType());
        System.out.println("Nationality: " + artist.getNationality());

        // Fetch and print associated albums
        System.out.println("Albums:");
        for (Album album : artist.getAlbums()) {
            System.out.println("Title: " + album.getTitle());
            System.out.println("Year: " + album.getYear());
            System.out.println("Rating: " + album.getRating());
            System.out.println("Is Active: " + album.isActive());
            System.out.println("----------------------");
        }
    }
}

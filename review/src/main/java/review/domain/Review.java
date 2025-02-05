package review.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import review.ReviewApplication;
import review.domain.ReviewAdded;
import review.domain.ReviewDeleted;
import review.domain.ReviewUpdated;

@Entity
@Table(name = "Review_table")
@Data
//<<< DDD / Aggregate Root
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String itemId;

    private Double rating;

    private String text;

    private String userId;

    @Lob
    private String userImg;

    @PostPersist
    public void onPostPersist() {
        ReviewAdded reviewAdded = new ReviewAdded(this);
        reviewAdded.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate() {
        ReviewUpdated reviewUpdated = new ReviewUpdated(this);
        reviewUpdated.publishAfterCommit();
    }

    @PreRemove
    public void onPreRemove() {
        ReviewDeleted reviewDeleted = new ReviewDeleted(this);
        reviewDeleted.publishAfterCommit();
    }
    
    public static ReviewRepository repository() {
        ReviewRepository reviewRepository = ReviewApplication.applicationContext.getBean(
            ReviewRepository.class
        );
        return reviewRepository;
    }
}
//>>> DDD / Aggregate Root

package review.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import review.domain.*;
import review.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class ReviewUpdated extends AbstractEvent {

    private Long id;
    private String itemId;
    private Double rating;
    private String text;
    private String userId;
    private String userImg;

    public ReviewUpdated(Review aggregate) {
        super(aggregate);
    }

    public ReviewUpdated() {
        super();
    }
}
//>>> DDD / Domain Event

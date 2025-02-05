package review.infra;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import review.domain.*;

@RepositoryRestResource(collectionResourceRel = "reviews_view", path = "reviews_view")
public interface ReviewsRepository
    extends PagingAndSortingRepository<Reviews, Long> {}
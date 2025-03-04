package uz.forall.telephone.payload;

import org.springframework.data.rest.core.config.Projection;
import uz.forall.telephone.entity.Category;

@Projection(name = "categoryView", types = Category.class)
public interface CategoryProjection {
    Long getId();
    String getName();
    Category getParent();
}


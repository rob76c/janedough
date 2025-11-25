package org.janedough.parent.repositories;
import org.janedough.parent.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName( String categoryName);
}

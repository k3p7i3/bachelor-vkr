create or replace function refresh_all_recommendation_scores()
returns void as $$
    declare
        global_avg_rating numeric;  -- C
        min_reviews integer := 10;  -- m
        base_rating numeric := 4.3; -- B
        weight_new integer := 5;    -- w
    begin
        -- Get global average rating (C)
        select avg(rating) into global_avg_rating from reviews;
        
        -- Fallback if no reviews exist
        if global_avg_rating is null then
            global_avg_rating := 4.0;
        end if;
        
        -- Update scores for all products (UPSERT)
        insert into recommendation_score (
            product_id,
            avg_rating,
            review_count,
            score,
            last_updated
        )
        select 
            p.product_id,
            coalesce(avg(r.rating), 0),
            count(r.rating),
            (coalesce(avg(r.rating), 0) * count(r.rating) + global_avg_rating * min_reviews + base_rating * weight_new) 
                / nullif(count(r.rating) + min_reviews + weight_new, 0),  -- Avoid division by zero
           now()
        from 
            products p
        left join
            reviews r on p.product_id = r.product_id
        group by
            p.product_id
        on conflict (product_id) 
        do update set
            avg_rating = excluded.avg_rating,
            review_count = excluded.review_count,
            score = excluded.score,
            last_updated = now();
    end;
$$ language plpgsql;
package com.example.vitabu;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ReviewTest {

    // test Review class
    Review review = new Review("Vitabu Owner", "Vitabu Borrower", 1, "Not a good borrower, doggy eared my books >:/", "Vitabu Owner", "Vitabu Borrower");

    @Test
    public void ReviewOwner_isCorrect() {
        assertEquals("Vitabu Owner", review.getOwnerName());
    }

    @Test
    public void ReviewBorrower_isCorrect() {
        assertEquals("Vitabu Borrower", review.getBorrowerName());
    }

    @Test
    public void ReviewRating_isCorrect() {
        assertEquals(1, review.getRating());
    }

    @Test
    public void ReviewBody_isCorrect() {
        assertEquals("Not a good borrower, doggy eared my books >:/", review.getBody());
    }

    @Test
    public void ReviewFrom_isCorrect() {
        assertEquals("Vitabu Owner", review.getReviewFrom());
    }

    @Test
    public void ReviewTo_isCorrect() {
        assertEquals("Vitabu Borrower", review.getReviewTo());
    }

    @Test
    public void ReviewNoOneElse_isCorrect(){
        // make sure ownerName is either the borrower or the owner
        // make sure borrowerNAme is either the borrower or the owner
        assertEquals(true, (review.getReviewFrom().equals(review.getOwnerName())) || (review.getReviewFrom().equals(review.getBorrowerName())));
        assertEquals(true, (review.getReviewTo().equals(review.getOwnerName())) || (review.getReviewTo().equals(review.getBorrowerName())));
    }
}

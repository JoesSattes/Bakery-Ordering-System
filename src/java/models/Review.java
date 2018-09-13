package models;

public class Review {
    private int reviewId;
    private int vote;
    private String comment;

    public Review() {
    }

    public Review(int reviewId, int vote) {
        this.reviewId = reviewId;
        this.vote = vote;
    }

    public Review(int reviewId, String comment) {
        this.reviewId = reviewId;
        this.comment = comment;
    }

    public Review(int reviewId, int vote, String comment) {
        this.reviewId = reviewId;
        this.vote = vote;
        this.comment = comment;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
}

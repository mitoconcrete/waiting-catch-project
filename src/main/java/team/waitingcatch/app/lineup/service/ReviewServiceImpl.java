package team.waitingcatch.app.lineup.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService, InternalReviewService {

	private final ReviewRepository repository;

}
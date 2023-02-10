package team.waitingcatch.app.lineup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.repository.ReviewRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService, InternalReviewService {

	private final ReviewRepository repository;

}
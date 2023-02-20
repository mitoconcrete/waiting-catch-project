package team.waitingcatch.app.user.service;

import team.waitingcatch.app.user.entitiy.User;

public interface InternalUserService {
	String _getUsernameById(Long id);

	User _getUserByUsername(String username);

	User _getUserByEmail(String email);

	User _getUserByUserId(Long id);

	void _deleteSellerAndRelatedInformation(Long userId);
}
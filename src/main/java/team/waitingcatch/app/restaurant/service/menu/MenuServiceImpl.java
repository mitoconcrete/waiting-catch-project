package team.waitingcatch.app.restaurant.service.menu;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuServiceImpl implements MenuService, InternalMenuService {
}

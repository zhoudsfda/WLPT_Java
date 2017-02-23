package pw.ewen.WLPT.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.web.bind.annotation.*;

import pw.ewen.WLPT.entity.User;
import pw.ewen.WLPT.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserRepository userRepository;
	
	@Autowired
	public UserController(UserRepository userRepository){
		this.userRepository = userRepository;
	}

	//获取所有用户
	@RequestMapping(value = "/all", method=RequestMethod.GET, produces="application/json")
	@PostFilter("hasPermission(filterObject, 'read')")
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}

	//获取用户（分页）
	@RequestMapping(method = RequestMethod.GET, produces="application/json")
	public Page<User> getUsersWithPage(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
									   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize){
		return userRepository.findAll(new PageRequest(pageIndex, pageSize, new Sort(Sort.Direction.ASC, "name")));
	}

	@RequestMapping(value="/{userId}", method=RequestMethod.GET, produces="application/json")
	public User getOneUser(@PathVariable("userId") String userId){
		return userRepository.findOne(userId);
	}

	@RequestMapping(method=RequestMethod.POST, produces="application/json")
	public User save(@RequestBody User user){
	    return this.userRepository.save(user);
    }

    @RequestMapping(value = "/{userIds}", method=RequestMethod.DELETE, produces = "application/json")
    public void delete(@PathVariable("userIds") String userIds){
		String[] arrUserIds = userIds.split(",");
		for(String id : arrUserIds){
			this.userRepository.delete(id);
		}

	}
}

package pw.ewen.WLPT.security.acl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.IdentityUnavailableException;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.stereotype.Component;
import pw.ewen.WLPT.domain.NeverMatchedResourceRange;
import pw.ewen.WLPT.domain.Resource;
import pw.ewen.WLPT.domain.entity.ResourceRange;
import pw.ewen.WLPT.domain.entity.Role;
import pw.ewen.WLPT.repository.ResourceRangeRepository;
import pw.ewen.WLPT.security.UserContext;

/**
 * Created by wen on 17-2-26.
 * 根据domain object 找到acl_object_identity中的objectidentity策略实现
 * 由于acl数据库中实际保存的是object_range，所以需要从domain_object到object_range进行转换
 * 根据domain_object查找符合要求的object_range
 */
@Component
public class ObjectIdentityRetrievalStrategyWLPTImpl implements ObjectIdentityRetrievalStrategy {
    @Autowired
    private UserContext userContext;
    @Autowired
    private ResourceRangeRepository resourceRangeRepository;

    @Override
    public ObjectIdentity getObjectIdentity(Object domainObject) {
        //查找ResourceRepository中当前SID对应的ResourceRange
        Role currentUserRole = userContext.getCurrentUser().getRole();
        ResourceRange resourceRange = getResourceRange(domainObject, currentUserRole);
        if(resourceRange == null){
            throw new IdentityUnavailableException("从Resource获取匹配的ResourceRange时出错，返回Null");
        }else{
            return new ObjectIdentityImpl(resourceRange);
        }
//        return null;
    }

    /**
     * 从domain object获得ResourceRange范围对象
     * @Return 匹配的ResourceRange，如果没有匹配对象则返回一个固定ResourceRange(任何用户不能对此ResourceRange有权限)
     */
    private ResourceRange getResourceRange(Object domainObject, Role role)  {
        ResourceRange range = new ResourceRange();
        if(domainObject instanceof Resource){
            ResourceRange matchedRange = range.matchRangeByResourceAndRole((Resource)domainObject, role, resourceRangeRepository);
            //如果没有匹配到返回一个Never_Matched_ResourceRange
            //没有匹配到ResourceRange代表该Role对资源没有访问权
            matchedRange = matchedRange == null ? new NeverMatchedResourceRange() : matchedRange;
            return  matchedRange;
        }else{
            throw new IllegalArgumentException("domainObject should be Resource Type");
        }

    }
}

package pw.ewen.WLPT.domain.DTO.ResourceRange;

import org.springframework.util.Assert;
import pw.ewen.WLPT.domain.DTO.DTOConvert;
import pw.ewen.WLPT.domain.entity.ResourceRange;
import pw.ewen.WLPT.domain.entity.ResourceType;
import pw.ewen.WLPT.domain.entity.Role;
import pw.ewen.WLPT.repository.ResourceTypeRepository;
import pw.ewen.WLPT.repository.RoleRepository;

/**
 * Created by wen on 17-4-9.
 */
public class ResourceRangeDTO {
    private long id;
    private String filter;
    private String roleId;
    private String resourceTypeClassName;
    private boolean matchAll = false;

    //实现DTOConvert接口的内部类
    private class ResourceRangeConverter implements DTOConvert<ResourceRangeDTO, ResourceRange>{

        private RoleRepository roleRepository;
        private ResourceTypeRepository resourceTypeRepository;

        private ResourceRange range;


        public ResourceRangeConverter(ResourceRange range){
            this.range = range;
        }

        public ResourceRangeConverter(RoleRepository roleRepository, ResourceTypeRepository resourceTypeRepository) {
            this.roleRepository = roleRepository;
            this.resourceTypeRepository = resourceTypeRepository;
        }

        @Override
        public ResourceRange doForward(ResourceRangeDTO dto) {
            Assert.notNull(this.roleRepository);
            Assert.notNull(this.resourceTypeRepository);

            ResourceRange range = new ResourceRange();
            range.setId(dto.getId());
            range.setFilter(dto.getFilter());
            range.setMatchAll(dto.isMatchAll());
            Role role = roleRepository.getOne(dto.getRoleId());
            if(role != null){
                range.setRole(role);
            }
            ResourceType type = resourceTypeRepository.getOne((dto.getResourceTypeClassName()));
            if(type != null){
                range.setResourceType(type);
            }
            return range;
        }

        @Override
        public ResourceRangeDTO doBackward(ResourceRange range) {
            ResourceRangeDTO dto = new ResourceRangeDTO();
            dto.setId(range.getId());
            dto.setFilter(range.getFilter());
            dto.setMatchAll(range.isMatchAll());
            dto.setResourceTypeClassName(range.getResourceType().getClassName());
            dto.setRoleId(range.getRole().getId());
            return dto;
        }
    }

    /**
     * 转化DTO为ResourceRange对象
     */
    public ResourceRange convertToResourceRange(RoleRepository roleRepository, ResourceTypeRepository resourceTypeRepository){
        ResourceRangeConverter converter = new ResourceRangeConverter(roleRepository, resourceTypeRepository);
        return converter.doForward(this);
    }

    /**
     * 转化ResourceRange对象为DTO对象
     */
    public ResourceRangeDTO convertFromResourceRange(ResourceRange range){
        ResourceRangeConverter converter = new ResourceRangeConverter(range);
        return converter.doBackward(range);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResourceTypeClassName() {
        return resourceTypeClassName;
    }

    public void setResourceTypeClassName(String resourceTypeClassName) {
        this.resourceTypeClassName = resourceTypeClassName;
    }

    public boolean isMatchAll() {
        return matchAll;
    }

    public void setMatchAll(boolean matchAll) {
        this.matchAll = matchAll;
    }
}

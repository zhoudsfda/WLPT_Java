package pw.ewen.WLPT.domain;

import pw.ewen.WLPT.domain.entity.ResourceRange;

/**
 * Created by wenliang on 17-3-9.
 * 当Resource找不到匹配的ResourceRange时，返回此ResourceRange对象，不能有任何角色对此ResourceRange有权限
 */
public class NeverMatchedResourceRange extends ResourceRange {
}

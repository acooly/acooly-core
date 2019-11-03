package com.acooly.core.test.utils.bean;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.arithmetic.tree.TreeNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhangpu
 * @date 2019-05-23 11:53
 */
@Getter
@Setter
public class MaskNode implements TreeNode<MaskNode> {

    private Long id;

    private Long parentId;

    private String name;

    @ToString.Maskable(maskType = Strings.MaskType.UserName)
    private String userName = "zhangpu" + id;

    @ToString.Maskable(maskType = Strings.MaskType.IdCardNo)
    private String idCardNo = "510221198209476371" + id;

    @ToString.Maskable(maskType = Strings.MaskType.MobileNo)
    private String mobileNo = "13896177630";

    private long sortTime;

    private List<MaskNode> children;

    private MaskNode maskNode;

    public MaskNode() {
    }

    public MaskNode(Long id, Long parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
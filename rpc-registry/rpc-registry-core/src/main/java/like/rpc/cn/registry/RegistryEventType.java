package like.rpc.cn.registry;

/**
 * 注册事件类型
 */
public enum RegistryEventType {
    /** 添加 */ PUT,
    /** 删除 */ DELETE,
    /** 未被认可的 */ UNRECOGNIZED;

    private RegistryEventType() {
    }
}

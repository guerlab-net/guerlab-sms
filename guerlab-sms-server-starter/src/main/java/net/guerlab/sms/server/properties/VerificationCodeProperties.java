package net.guerlab.sms.server.properties;

/**
 * 验证码配置
 *
 * @author guer
 *
 */
public class VerificationCodeProperties {

    /**
     * 验证码过期时间,小于等于0表示不过期
     */
    private Long expirationTime;

    /**
     * 验证码长度
     */
    private int codeLength = 6;

    /**
     * 是否使用识别码
     */
    private boolean useIdentificationCode = false;

    /**
     * 识别码长度
     */
    private int identificationCodeLength = 3;

    /**
     * 验证成功是否删除验证码
     */
    private boolean deleteByVerifySucceed = true;

    /**
     * 验证失败是否删除验证码
     */
    private boolean deleteByVerifyFail = false;

    /**
     * 返回 验证码过期时间，单位秒
     *
     * @return 验证码过期时间
     */
    public Long getExpirationTime() {
        return expirationTime;
    }

    /**
     * 设置验证码过期时间，单位秒
     *
     * @param expirationTime
     *            验证码过期时间，小于等于0表示不过期
     */
    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * 返回验证码长度
     *
     * @return 验证码长度
     */
    public int getCodeLength() {
        return codeLength;
    }

    /**
     * 设置验证码长度
     *
     * @param codeLength
     *            验证码长度
     */
    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    /**
     * 返回是否使用识别码
     *
     * @return 是否使用识别码
     */
    public boolean isUseIdentificationCode() {
        return useIdentificationCode;
    }

    /**
     * 设置是否使用识别码
     *
     * @param useIdentificationCode
     *            是否使用识别码
     */
    public void setUseIdentificationCode(boolean useIdentificationCode) {
        this.useIdentificationCode = useIdentificationCode;
    }

    /**
     * 返回识别码长度
     *
     * @return 识别码长度
     */
    public int getIdentificationCodeLength() {
        return identificationCodeLength;
    }

    /**
     * 设置识别码长度
     *
     * @param identificationCodeLength
     *            识别码长度
     */
    public void setIdentificationCodeLength(int identificationCodeLength) {
        this.identificationCodeLength = identificationCodeLength;
    }

    /**
     * 返回验证成功是否删除验证码
     *
     * @return 验证成功是否删除验证码
     */
    public boolean isDeleteByVerifySucceed() {
        return deleteByVerifySucceed;
    }

    /**
     * 设置验证成功是否删除验证码
     *
     * @param deleteByVerifySucceed
     *            验证成功是否删除验证码
     */
    public void setDeleteByVerifySucceed(boolean deleteByVerifySucceed) {
        this.deleteByVerifySucceed = deleteByVerifySucceed;
    }

    /**
     * 返回验证失败是否删除验证码
     *
     * @return 验证失败是否删除验证码
     */
    public boolean isDeleteByVerifyFail() {
        return deleteByVerifyFail;
    }

    /**
     * 设置验证失败是否删除验证码
     *
     * @param deleteByVerifyFail
     *            验证失败是否删除验证码
     */
    public void setDeleteByVerifyFail(boolean deleteByVerifyFail) {
        this.deleteByVerifyFail = deleteByVerifyFail;
    }
}

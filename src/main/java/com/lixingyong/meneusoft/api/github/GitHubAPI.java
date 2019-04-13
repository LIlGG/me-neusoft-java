package com.lixingyong.meneusoft.api.github;

public final class GitHubAPI {
    /** github存储库接口 */
    public static final String GITHUB = "https://api.github.com/repos/LIlGG/MeNeusoft";
    /** issues接口 */
    public static final String ISSUES = GITHUB + "/issues";
    /** 获取详细的issue信息 */
    public static final String ISSUE_DETAIL = ISSUES + "/{issueId}";
    /** 获取issue的评论信息 */
    public static final String ISSUE_COMMENTS = ISSUE_DETAIL + "/comments";
}

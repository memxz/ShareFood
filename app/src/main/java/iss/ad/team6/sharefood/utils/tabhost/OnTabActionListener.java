package iss.ad.team6.sharefood.utils.tabhost;

public interface OnTabActionListener {
    /**
     * Weather consume this action.
     *
     * @param position tab_id
     * @return true or false
     */
    public boolean onTabClick(int position);

    /**
     * Tab is selected.
     *
     * @param position tab_id
     */
    public void onTabSelected(int position);
}

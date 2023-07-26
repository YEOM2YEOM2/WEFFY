import React, { useState } from "react";
import styles from "./sidebar.module.css";

//이미지
import defaultImg from "../../assets/images/defualt_image.png";
import dashboardIcon from "../../assets/images/icon_dashboard.png";
import meetingIcon from "../../assets/images/icon_mymeeting.png";
import settingIcon from "../../assets/images/icon_setting.png";
import logoutIcon from "../../assets/images/icon_logout.png";

// import profileImg from ".\assets\images\impo_eunjjin.jpg"

const Sidebar = (props) => {
  const handleDashboardClick = () => {
    console.log("Dashboard was clicked");
    // Dashboard 클릭 시 수행할 동작
  };

  const handleMyMeetingClick = () => {
    console.log("My Meeting was clicked");
    // My Meeting 클릭 시 수행할 동작
  };

  const handleSettingClick = () => {
    console.log("Setting was clicked");
    // Profile 클릭 시 수행할 동작
  };

  const handleLogoutClick = () => {
    console.log("Logout was clicked");
    // Profile 클릭 시 수행할 동작
  };

  return (
    <div className={styles["container"]}>
      <div className={styles["sidebar"]}>
        <div className={styles["profileframe"]}>
          <img
            src={defaultImg}
            alt="profileImg"
            className={styles["profileImg"]}
          />
          <span className={styles["text"]}>
            <span>Silver.jjin</span>
          </span>
        </div>
        {/* dashboard button */}
        <div className={styles["functions"]}>
          <img src alt="selectbar" className={styles["selectbar"]} />
          <div className={styles["dashboard"]} onClick={handleDashboardClick}>
            <img
              src={dashboardIcon}
              alt="dachboard"
              className={styles["icon"]}
            />
            <span className={styles["bar_text"]}>
              <span>Dashboard</span>
            </span>
          </div>
          {/* mymeeting button */}
          {/* mymeeting button */}
          <div className={styles["my-list"]} onClick={handleMyMeetingClick}>
            <img
              src={meetingIcon}
              alt="setting"
              className={styles["icon_list"]}
            />
            <span className={styles["bar_text"]}>
              <span>My Meeting</span>
            </span>
          </div>

          {/* setting button */}
          <div className={styles["setting"]} onClick={handleSettingClick}>
            <img src={settingIcon} alt="setting" className={styles["icon"]} />
            <span className={styles["bar_text"]}>
              <span>Settings</span>
            </span>
          </div>
          {/* logout button */}
          <div className={styles["logout"]} onClick={handleLogoutClick}>
            <img src={logoutIcon} alt="logout" className={styles["icon"]} />
            <span className={styles["bar_text"]}>
              <span>logout</span>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;

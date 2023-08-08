import { useNavigate } from "react-router-dom";
import styles from "./notFound404.module.css";

function NotFount404() {
  let navigate = useNavigate();

  return (
    <body className={styles.body}>
      <div className={styles.errorPage}>
        <div className={styles.content}>
          <h2 className={styles.header} data-text="404">
            404
          </h2>
          <h4 className={styles.header} data-text="Page not found"></h4>
          <h4 className={styles.headerContent}>현재 접속하신 페이지는 없는 페이지입니다.</h4>
          <p>아래의 버튼을 통해 메인 화면으로 이동해주세요.</p>
          <div className={styles.btns}>
            <a className={styles.button} onClick={() => navigate("/im")}>
              돌아가기
            </a>
          </div>
        </div>
      </div>
    </body>
  )
}

export default NotFount404;
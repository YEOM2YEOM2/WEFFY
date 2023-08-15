// react 라이브러리
import * as React from 'react';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';

import axios from 'axios';
import Swal from 'sweetalert2';

// css
import styles from './loginForm.module.css';

// mui & bootstrap
import Button from '@mui/material/Button';
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';

// store user 함수
import { setIdentification, setAccessToken, setRefreshToken, setCsrfToken, setProfileImg, setNickname, setId } from '../../store/reducers/user';
import { identifier } from '@babel/types';

function LoginForm() {

  let navigate = useNavigate();
  let dispatch = useDispatch();

  let [email, setEmail] = useState("");
  let [pw, setPw] = useState("");
  let [cnt, setCnt] = useState(0);

  let [emailModal, setEmailModal] = useState(false);
  let [pwModal, setPasswordModal] = useState(false);


  const handleEmail = (e) => {
    setEmail(e.target.value);
  }

  const handlePw = (e) => {
    setPw(e.target.value);
  }
  
  useEffect(() => {
      if (cnt !== 0 && cnt < 10) {
          Swal.fire({
              icon: 'error',
              html: `<div style="font-family:GmarketSans">이메일/비밀번호가 올바르지 않습니다.<br>(${cnt}회 오입력하셨습니다.)</div>`,
              confirmButtonText: 'OK',
              confirmButtonColor: '#2672B9',
          })   
      } else if (cnt >= 10) {
          Swal.fire({
              icon: 'error',
              html: `<div style="font-family:GmarketSans">10회 이상 이메일/비밀번호를 오입력하셨습니다.<br>비밀번호를 재설정해주세요.</div>`,
              confirmButtonText: 'OK',
              confirmButtonColor: '#2672B9',
          })  
      }
  }, [cnt])

  const handleKeyDown = (e) => {
      if (e.keyCode === 13) {
        handleLogin()
      }
  }

  const handleLogin = () => {
    if (!email.trim() ) {
      Swal.fire({
        icon: 'question',
        html: '<div style="font-family:GmarketSans">Email을 입력해주세요.</div>',
        confirmButtonText: 'OK',
        confirmButtonColor: '#2672B9',
      })
      return
    } else if (!pw.trim()) {
      Swal.fire({
        icon: 'question',
        html: '<div style="font-family:GmarketSans">비밀번호를 입력해주세요.</div>',
        confirmButtonText: 'OK',
        confirmButtonColor: '#2672B9',
      })
      return
    }

    axios({
      method: 'post',
      url: 'http://i9d107.p.ssafy.io:8081/api/v1/users/signin',
      headers: {
        'accept': 'application/json',
        'Content-Type': 'application/json',
      },
      data: {
        email: email,
        password: pw
      }
    }).then((res)=> {
      dispatch(setIdentification(res.data.data.identification))
      dispatch(setAccessToken(res.data.data.token.accessToken))
      dispatch(setRefreshToken(res.data.data.token.refreshToken))
      dispatch(setCsrfToken(res.data.data.token.csrfToken))
      dispatch(setProfileImg(res.data.data.profileImg))
      dispatch(setNickname(res.data.data.nickName))
      dispatch(setId(email.split('@')[0]))
      navigate("/im")
    }).catch((err)=>{
      setCnt(cnt+1)
    })
    
  }
  

  return (
    <div className={styles.login_form_wrapper}>
      <div className={styles.login_form} style={{ position: 'relative', zIndex: 0 }}>
        <p className={styles.logo} style={{ position: 'relative', zIndex: 1 }}>WEEFY</p>
        <Form className={styles.custom_form}  style={{ position: 'relative', zIndex: 1 }}>
          <Form.Group className="mb-3" controlId="exampleForm.ControlInput">
            <Form.Label style={{marginLeft: '4px'}}>Email</Form.Label>
            <Form.Control type="email" placeholder="Mattermost Email"
            onChange={handleEmail} />
          </Form.Group>
          <Form.Group className="mb-3" controlId="exampleForm.ControlPassword">
            <Form.Label style={{marginLeft: '4px'}}>Password</Form.Label>
            <Form.Control type="password" placeholder="Mattermost Password" 
            onChange={handlePw} onKeyDown={handleKeyDown}
            />
          </Form.Group>
          <div className={styles.forgot_password}>
            <Button variant="text" href="https://meeting.ssafy.com/reset_password" style={{ textDecorationLine: 'none', color: 'black', fontFamily: 'Poppins' }}>Forgot Password?</Button>
          </div>
          <Button variant="contained" size="large" className={styles.login_btn} style={{ fontSize: '20px', fontFamily: 'Poppins', fontWeight: '600', backgroundColor: '#2672B9' }}
          onClick={handleLogin}>
            Login
          </Button>
          <Container className={styles.sign}>
              <p style={{ marginBottom: '0', marginTop: '9px' }}>You don't have a account?</p>
              <Button variant="text" onClick={()=> {navigate('/signup');}}
              style={{ textDecorationLine: 'none', color: '#2672B9', fontFamily: 'Poppins', fontSize: '17px' }}>
                Sign up
              </Button>
          </Container>
        </Form>
      </div>
    </div>
  )
}

export default LoginForm
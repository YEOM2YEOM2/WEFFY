import React from 'react';
import Swal from 'sweetalert2';
import axios from 'axios';
import styles from './signupWide.module.css'

// mui
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import FormHelperText from '@mui/material/FormHelperText';
import FormControl from '@mui/material/FormControl';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import Checkbox from '@mui/material/Checkbox';

// bootstrap
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';

// hook
import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';

// store user 함수


const label = { inputProps: { 'aria-label': 'Checkbox demo' } };

function SignupWide() {
    
    let navigate = useNavigate();

    let [email, setEmail] = useState("");
    let [pw, setPw] = useState("");
    let [agree, setAgree] = useState(false);
    let [cnt, setCnt] = useState(0);

    const handleEmail = (e) => {
        setEmail(e.target.value);
    }

    const handlePw = (e) => {
        setPw(e.target.value);
    }

    const handleCheck = (e) => {
        setAgree(!agree)
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
          handleSignup()
        }
    }

    const handleSignup = () => {
        if (!email.trim() ) {
          Swal.fire({
            icon: 'question',
            html: '<div style="font-family:GmarketSans">Email을 입력해주세요.</div>',
            confirmButtonText: 'OK',
            confirmButtonColor: '#2672B9',
          })
          return
        }
        
        if (!pw.trim()) {
          Swal.fire({
            icon: 'question',
            html: '<div style="font-family:GmarketSans">비밀번호를 입력해주세요.<br>(공백은 입력되지 않습니다.)</div>',
            confirmButtonText: 'OK',
            confirmButtonColor: '#2672B9',
          })
          return
        } 

        if (!agree) {
            Swal.fire({
                icon: 'error',
                html: '<div style="font-family:GmarketSans">개인정보제공에 동의해주세요.</div>',
                confirmButtonText: 'OK',
                confirmButtonColor: '#2672B9',
            })
            return
        }

        axios({
            method: 'post',
            url: 'http://i9d107.p.ssafy.io:8081/api/v1/users/signup',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
            },
            data: {
              email: email,
              password: pw
            }
          }).then((res)=> {
            if (res.data.status === 201) {
                Swal.fire({
                    icon: 'success',
                    html: '<div style="font-family:GmarketSans">회원가입이 완료되었습니다.</div>',
                    confirmButtonText: 'OK',
                    confirmButtonColor: '#2672B9',
                })
                navigate("/")
            }
          }).catch((err)=>{
            if (err.response.data.errorCode === 4000) {
                Swal.fire({
                    icon: 'warning',
                    html: '<div style="font-family:GmarketSans">회원가입이 이미 완료된 회원입니다.</div>',
                    confirmButtonText: 'OK',
                    confirmButtonColor: '#2672B9',
                })
                navigate("/")
            } else if (err.response.data.errorCode === 4004) {
                setCnt(cnt+1)
            }
        })
    }

    const [showPassword, setShowPassword] = React.useState(false);

    const handleClickShowPassword = () => setShowPassword((show) => !show);

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };


    return (
    <div className={styles.wide}>
        <p className={styles.title}>Sign Up</p>
        <Container>
            <Row className={styles.margin}>
                <FormControl style={{ width: '100%' }} sx={{ m: 1, width: '25ch' }} variant="outlined">
                    <InputLabel htmlFor="outlined-adornment-email" style={{ fontFamily: 'Poppins' }}>Email</InputLabel>
                    <OutlinedInput
                        id="outlined-adornment-email"
                        type={showPassword ? 'text' : 'email'}
                        label="email" style={{ fontFamily: 'Poppins' }}
                        onChange={handleEmail}
                    />
                <FormHelperText id="outlined-weight-helper-text" style={{ fontFamily: 'NanumSquareNeo', fontWeight: '600' }}>Mattermost Email을 입력해주세요.</FormHelperText>
                </FormControl>
            </Row>
            <Row className={styles.margin}>
                <FormControl style={{ width: '100%' }} sx={{ m: 1, width: '25ch' }} variant="outlined">
                    <InputLabel htmlFor="outlined-adornment-password" style={{ fontFamily: 'Poppins' }}>Password</InputLabel>
                    <OutlinedInput
                        id="outlined-adornment-password"
                        type={showPassword ? 'text' : 'password'}
                        endAdornment={
                        <InputAdornment position="end">
                            <IconButton
                            aria-label="toggle password visibility"
                            onClick={handleClickShowPassword}
                            onMouseDown={handleMouseDownPassword}
                            edge="end"
                            >
                            {showPassword ? <Visibility /> : <VisibilityOff />}
                            </IconButton>
                        </InputAdornment>
                        }
                        label="Password" style={{ fontFamily: 'Poppins' }}
                        onChange={handlePw}
                        onKeyDown={handleKeyDown}
                    />
                <FormHelperText id="outlined-weight-helper-text" style={{ fontFamily: 'NanumSquareNeo', fontWeight: '600' }}>Mattermost Password을 입력해주세요.</FormHelperText>
                </FormControl>
            </Row>
            <Row className={styles.forgot_password}>
                <div className={styles.forgot_password}>
                    <Button variant="text" href="https://meeting.ssafy.com/reset_password" style={{ textDecorationLine: 'none', color: 'black', fontFamily: 'Poppins' }}>Forgot Password?</Button>
                </div>
            </Row>
            <Row>
                <span style={{ fontFamily: 'NanumSquareNeo', fontWeight: '600' }}>
                    개인정보 수집 및 이용 동의(이메일, 이름, 별명, 프로필이미지 등)
                    <Checkbox {...label} onClick={handleCheck}/>
                </span>
            </Row>
        </Container>
        <Button variant="contained" size="large" 
        className={styles.createBtn} style={{ fontFamily: 'HindGunturBold', backgroundColor: '#2672B9', paddingTop: '10px', marginTop:'20px' }}
        onClick={handleSignup}>
            Create Account
        </Button>
    </div>
    )
}

export default SignupWide
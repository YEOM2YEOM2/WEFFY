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
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

// hook
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';

const label = { inputProps: { 'aria-label': 'Checkbox demo' } };

function SignupWide() {
    
    let navigate = useNavigate();

    const [showPassword, setShowPassword] = React.useState(false);
    const [showPasswordRe, setShowPasswordRe] = React.useState(false);

    let [email, setEmail] = useState("");
    let [pw, setPw] = useState("");
    let [rePw, setRePw] = useState("");
    let [agree, setAgree] = useState(false);

    const handleEmail = (e) => {
        setEmail(e.target.value);
    }

    const handlePw = (e) => {
        setPw(e.target.value);
    }

    const handleRePw = (e) => {
        setRePw(e.target.value);
    }

    const handleCheck = (e) => {
        setAgree(!agree)
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
        
        if (!pw.trim() || !rePw.trim()) {
          Swal.fire({
            icon: 'question',
            html: '<div style="font-family:GmarketSans">비밀번호를 입력해주세요.<br>(공백은 입력되지 않습니다.)</div>',
            confirmButtonText: 'OK',
            confirmButtonColor: '#2672B9',
          })
          return
        } 

        if (pw !== rePw ) {
            Swal.fire({
                icon: 'error',
                html: '<div style="font-family:GmarketSans">비밀번호가 일치하지 않습니다.</div>',
                confirmButtonText: 'OK',
                confirmButtonColor: '#AE2424',
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
            url: 'http://i9d107.p.ssafy.io:8081/api/v1/users/signin',
            // header : {
      
            // },
            data: {
              email: email,
              password: pw
            }
          }).then((res)=> {
            console.log(res)
          }).catch((err)=>{
            console.log(err)
          })
    }

    const handleClickShowPassword = () => setShowPassword((show) => !show);
    const handleClickShowPasswordRe = () => setShowPasswordRe((show) => !show);

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };
    const handleMouseDownPasswordRe = (event) => {
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
                    />
                <FormHelperText id="outlined-weight-helper-text" style={{ fontFamily: 'NanumSquareNeo', fontWeight: '600' }}>Mattermost Password을 입력해주세요.</FormHelperText>
                </FormControl>
            </Row>
            <Row className={styles.margin}>
                <FormControl style={{ width: '100%' }} sx={{ m: 1, width: '25ch' }} variant="outlined">
                    <InputLabel htmlFor="outlined-adornment-password" style={{ fontFamily: 'Poppins' }}>Password Confirmation</InputLabel>
                    <OutlinedInput
                        id="outlined-adornment-password"
                        type={showPasswordRe ? 'text' : 'password'}
                        endAdornment={
                        <InputAdornment position="end">
                            <IconButton
                            aria-label="toggle password visibility"
                            onClick={handleClickShowPasswordRe}
                            onMouseDown={handleMouseDownPasswordRe}
                            edge="end"
                            >
                            {showPasswordRe ? <Visibility /> : <VisibilityOff />}
                            </IconButton>
                        </InputAdornment>
                        }
                        onChange={handleRePw}
                        label="Password Confirmation"
                        style={{ fontFamily: 'Poppins' }}
                    />
                <FormHelperText id="outlined-weight-helper-text" style={{ fontFamily: 'NanumSquareNeo', fontWeight: '600' }}>비밀번호를 재입력해주세요.</FormHelperText>
                </FormControl>
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
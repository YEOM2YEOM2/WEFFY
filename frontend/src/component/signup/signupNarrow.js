import React from 'react';
import styles from './signupNarrow.module.css';

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
import { useNavigate } from 'react-router';


const label = { inputProps: { 'aria-label': 'Checkbox demo' } };

function SignupNarrow() {

    let navigate = useNavigate();

    const [showPassword, setShowPassword] = React.useState(false);
    const [showPasswordRe, setShowPasswordRe] = React.useState(false);

    const handleClickShowPassword = () => setShowPassword((show) => !show);
    const handleClickShowPasswordRe = () => setShowPasswordRe((show) => !show);

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };
    const handleMouseDownPasswordRe = (event) => {
        event.preventDefault();
    };
    
  return (
    <div className={styles.narrow}>
        <div className={styles.signup_form_wrapper}>
            <div className={styles.signup_form}>
                <p className={styles.logo}>WEFFY</p>
                <Container>
                    <Row className={styles.margin}>
                        <FormControl style={{ width: '100%' }} sx={{ m: 1, width: '25ch' }} variant="outlined">
                            <InputLabel htmlFor="outlined-adornment-email" style={{ fontFamily: 'Poppins' }}>Email</InputLabel>
                            <OutlinedInput
                                id="outlined-adornment-email"
                                type={showPassword ? 'text' : 'email'}
                                label="email" style={{ fontFamily: 'Poppins' }}
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
                                label="Password"
                                style={{ fontFamily: 'Poppins' }}
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
                                label="Password Confirmation"
                                style={{ fontFamily: 'Poppins' }}
                            />
                        <FormHelperText id="outlined-weight-helper-text" style={{ fontFamily: 'NanumSquareNeo', fontWeight: '600' }}>비밀번호를 재입력해주세요.</FormHelperText>
                        </FormControl>
                    </Row>
                    <Row>
                        <span style={{ fontFamily: 'NanumSquareNeo', fontWeight: '600', fontSize: '13px' }}>
                            개인정보 수집 및 이용 동의(이메일, 이름, 별명, 프로필이미지 등)
                            <Checkbox {...label}/>
                        </span>
                    </Row>
                    <Row className="justify-content-center">
                        <Button variant="contained" size="large" 
                        className={styles.createBtn} style={{ fontFamily: 'HindGunturBold', backgroundColor: '#2672B9', paddingTop: '10px',
                        marginTop:'20px' }}
                        onClick={()=> {navigate('/')}}>
                            Create Account
                        </Button>
                    </Row>
                </Container>
                
                
            </div>
        </div>
    </div>
  )
}

export default SignupNarrow
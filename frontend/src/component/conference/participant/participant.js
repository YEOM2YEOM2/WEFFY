import React from 'react'

// mui
import MicIcon from '@mui/icons-material/Mic';
import MicOffIcon from '@mui/icons-material/MicOff';
import VideocamIcon from '@mui/icons-material/Videocam';
import VideocamOffIcon from '@mui/icons-material/VideocamOff';

function Participant(props) {
  let {user, participants, handleNickname } = props;

  console.log('me : ', user)

  // participants.map((person) => {
  //   console.log(person)
  //   if (person.isLocal()) {
  //     console.log("왜 undefined!!!!!!!")
  // }
  // })



  return (
    <div style={{ color: "white" }}>
      {/* 나 */}
      <div>
        <span>{ user.getNickname() }</span>
        { user.videoActive ? <VideocamIcon fontSize='small' />: <VideocamOffIcon fontSize='small' style={{ color: "red" }} /> }
            { user.audioActive ? <MicIcon fontSize='small' />: <MicOffIcon fontSize='small' style={{ color: "red" }} /> }
      </div>
      {/* 다른 사용자 */}
      { participants.map((person, idx) => (
          <div key={idx}>
            <span>{ person.getNickname() }</span>
            { person.videoActive ? <VideocamIcon fontSize='small' />: <VideocamOffIcon fontSize='small' style={{ color: "red" }} /> }
            { person.audioActive ? <MicIcon fontSize='small' />: <MicOffIcon fontSize='small' style={{ color: "red" }} /> }
          </div>
      ))}
    </div>
  )
}

export default Participant;
import React from 'react'

// mui
import MicIcon from '@mui/icons-material/Mic';
import MicOffIcon from '@mui/icons-material/MicOff';
import VideocamIcon from '@mui/icons-material/Videocam';
import VideocamOffIcon from '@mui/icons-material/VideocamOff';

function Participant(props) {
  let { participants, handleNickname } = props;

  participants.map((val) => {
    console.log(val)
  })

  return (
    <div style={{ color: "white" }}>
      { participants.map((person, idx) => (
          <div key={idx}>
            <span>{ person.getNickname() }</span>
            { person.videoActive ? <VideocamIcon />: <VideocamOffIcon /> }
            { person.audioActive ? <MicIcon />: <MicOffIcon /> }
          </div>
      ))}
    </div>
  )
}

export default Participant;
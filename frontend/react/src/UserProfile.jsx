const UserProfile = ({ gender, imgNumber }) => {
    return (
        <img alt={"profile"} src={`https://randomuser.me/api/portraits/med/${gender}/${imgNumber}.jpg`}/>
    )
}

export default UserProfile
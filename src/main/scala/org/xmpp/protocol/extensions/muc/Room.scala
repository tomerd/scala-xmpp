package org.xmpp
{
	package protocol.extensions.muc
	{
		// TODO: implement this
		// see http://xmpp.org/extensions/xep-0045.html
		trait Room 
		{
			/*
			4.2 Room Types

			Fully-Anonymous Room
			A room in which the full JIDs or bare JIDs of occupants cannot be discovered by anyone, including room admins and room owners; such rooms are NOT RECOMMENDED or explicitly supported by MUC, but are possible using this protocol if a service implementation offers the appropriate configuration options; contrast with Non-Anonymous Room and Semi-Anonymous Room.
			Hidden Room
			A room that cannot be found by any user through normal means such as searching and service discovery; antonym: Public Room.
			Members-Only Room
			A room that a user cannot enter without being on the member list; antonym: Open Room.
			Moderated Room
			A room in which only those with "voice" may send messages to all occupants; antonym: Unmoderated Room.
			Non-Anonymous Room
			A room in which an occupant's full JID is exposed to all other occupants, although the occupant may choose any desired room nickname; contrast with Semi-Anonymous Room and Fully-Anonymous Room.
			Open Room
			A room that anyone may enter without being on the member list; antonym: Members-Only Room.
			Password-Protected Room
			A room that a user cannot enter without first providing the correct password; antonym: Unsecured Room.
			Persistent Room
			A room that is not destroyed if the last occupant exits; antonym: Temporary Room.
			Public Room
			A room that can be found by any user through normal means such as searching and service discovery; antonym: Hidden Room.
			Semi-Anonymous Room
			A room in which an occupant's full JID can be discovered by room admins only; contrast with Fully-Anonymous Room and Non-Anonymous Room.
			Temporary Room
			A room that is destroyed if the last occupant exits; antonym: Persistent Room.
			Unmoderated Room
			A room in which any occupant is allowed to send messages to all occupants; antonym: Moderated Room.
			Unsecured Room
			A room that anyone is allowed to enter without first providing the correct password; antonym: Password-Protected Room.
			*/
			
			/*
			Table 3: Privileges Associated With Roles

			Privilege	None	Visitor	Participant	Moderator
			Present in Room	No	Yes	Yes	Yes
			Receive Messages	No	Yes	Yes	Yes
			Receive Occupant Presence	No	Yes	Yes	Yes
			Presence Broadcasted to Room	No	Yes*	Yes	Yes
			Change Availability Status	No	Yes	Yes	Yes
			Change Room Nickname	No	Yes*	Yes	Yes
			Send Private Messages	No	Yes*	Yes	Yes
			Invite Other Users	No	Yes*	Yes*	Yes
			Send Messages to All	No	No**	Yes	Yes
			Modify Subject	No	No*	Yes*	Yes
			Kick Participants and Visitors	No	No	No	Yes
			Grant Voice	No	No	No	Yes
			Revoke Voice	No	No	No	Yes***
			*/
			
			/*
			Table 4: Role State Chart

			>	None	Visitor	Participant	Moderator
			None	--	Enter moderated room	Enter unmoderated room	Admin or owner enters room
			Visitor	Exit room or be kicked by a moderator	--	Moderator grants voice	Admin or owner grants moderator privileges
			Participant	Exit room or be kicked by a moderator	Moderator revokes voice	--	Admin or owner grants moderator privileges
			Moderator	Exit room	Admin or owner changes role to visitor *	Admin or owner changes role to participant or revokes moderator privileges *	--			
			*/
			
			
			/*
			Table 5: Privileges Associated With Affiliations

			Privilege	Outcast	None	Member	Admin	Owner
			Enter Open Room	No	Yes*	Yes	Yes	Yes
			Register with Open Room	No	Yes	N/A	N/A	N/A
			Retrieve Member List	No	No**	Yes	Yes	Yes
			Enter Members-Only Room	No	No	Yes*	Yes	Yes
			Ban Members and Unaffiliated Users	No	No	No	Yes	Yes
			Edit Member List	No	No	No	Yes	Yes
			Edit Moderator List	No	No	No	Yes**	Yes**
			Edit Admin List	No	No	No	No	Yes
			Edit Owner List	No	No	No	No	Yes
			Change Room Definition	No	No	No	No	Yes
			Destroy Room	No	No	No	No	Yes
			*/
			
			/*
			Table 6: Affiliation State Chart

			>	Outcast	None	Member	Admin	Owner
			Outcast	--	Admin or owner removes ban	Admin or owner adds user to member list	Owner adds user to admin list	Owner adds user to owner list
			None	Admin or owner applies ban	--	Admin or owner adds user to member list, or user registers as member (if allowed)	Owner adds user to admin list	Owner adds user to owner list
			Member	Admin or owner applies ban	Admin or owner changes affiliation to "none"	--	Owner adds user to admin list	Owner adds user to owner list
			Admin	Owner applies ban	Owner changes affiliation to "none"	Owner changes affiliation to "member"	--	Owner adds user to owner list
			Owner	Owner applies ban	Owner changes affiliation to "none"	Owner changes affiliation to "member"	Owner changes affiliation to "admin"	--
			*/
			
		}
	}
}
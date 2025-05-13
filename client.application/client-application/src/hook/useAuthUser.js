import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { selectAuthData } from "../feature/authSlice";
import { selectAuthUser, fetchAuthUser } from "../feature/user/userSlice";

function useAuthUser() {
  const authData = useSelector(selectAuthData);
  const authUser = useSelector(selectAuthUser);
  const dispatch = useDispatch();

  useEffect(() => {
    if (authData && !authUser) {
        dispatch(fetchAuthUser({email: authData.email}));
    }
  }, [authData, authUser, dispatch]);

  return authUser;
}

export default useAuthUser;
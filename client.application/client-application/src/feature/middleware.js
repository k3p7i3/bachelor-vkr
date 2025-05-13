import { setAccessToken } from '../api/httpRequest';

export const middleware = async (method, params, thunk, { inputMapper, outputMapper }, useAuth = true) => {
    if (useAuth) {
      setAccessToken(thunk.getState().auth.data.accessToken);
    }
    var mappedParams = map(inputMapper, params);
    var response = await handleErrors(method, mappedParams, thunk.rejectWithValue);
    var mappedResponse = map(outputMapper, response);
    return mappedResponse;
}

const handleErrors = async (method, params, rejectWithValue) => {
    return await method(params)
        .then((response => {
            return response.data;
        }))
        .catch(error => {
            if (error.response) {
                var dataError = error.response.data.error ? error.response.data.error : error.response.data;
                var message = dataError ? (": " + dataError) : "";
                return rejectWithValue({ message: message, code: error.response.status });
            }
            return rejectWithValue({ message: ": " + error.message, code: 0 });
        })
}

const map = (mapper, params) => {
    if (mapper) {
        return mapper(params);
    }
    return params;
}

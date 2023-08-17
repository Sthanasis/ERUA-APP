import {
  IAllSolutionsResponse,
  IEruaMemberSolution,
  IExhibitionResponse,
  ISolutionPayload,
  IStakeholderProblem,
  IUserResponse,
} from '~/types/responseData';
import { deleteRequest, get, getBlob, post, postBlob } from './base';
import { useUserStore } from '~/pinia/modules/user';
import { ICreateExhibitionBody } from '~/types/postData';

const baseUrl = '/eruamember';

const getFriendList = async () => {
  const { email } = useUserStore();
  return await get<IUserResponse[]>(`${baseUrl}/${email}/friends`);
};

const createSolution = async (payload: ISolutionPayload) => {
  const { email } = useUserStore();
  return await post<unknown>(`${baseUrl}/${email}/solutions`, payload);
};

const getAllSolutions = async (search?: string) => {
  let endpoint = `${baseUrl}/solutions`;
  if (search) {
    endpoint = `${endpoint}?contain=${search}`;
  }
  return await get<IAllSolutionsResponse<IEruaMemberSolution[]>>(endpoint);
};

const getEruaMember = async (email: string) => {
  return await get<IUserResponse[]>(`${baseUrl}/${email}`);
};

const deleteSolution = async (solutionId: number) => {
  return await deleteRequest(`${baseUrl}/solutions?solutionId=${solutionId}`);
};

const getSolutionsByMember = async (email: string) => {
  return await get<IEruaMemberSolution[]>(`${baseUrl}/${email}/solutions`);
};

const getProblemsBySolution = async (solutionId: number) => {
  return await get<IStakeholderProblem[]>(`${baseUrl}/${solutionId}/problems`);
};

const addSolutionToProblem = async (solutionId: number, problemId: number) => {
  return await get(
    `${baseUrl}/addsolutiontoproblem?solutionId=${solutionId}&problemId=${problemId}`
  );
};

const getUnlinkedProblemsBySolution = async (solutionId: number) => {
  return await get<IStakeholderProblem[]>(
    `${baseUrl}/solutions/notlinkedproblems?solutionId=${solutionId}`
  );
};

const createExhibition = async (data: ICreateExhibitionBody) => {
  return await post(`${baseUrl}/exhibition`, data);
};

const getExhibitions = async () => {
  return await get<IExhibitionResponse[]>(`${baseUrl}/exhibition/all`);
};

const getExhibition = async (id: string) => {
  return await get<IExhibitionResponse>(
    `${baseUrl}/exhibition?exhibitionId=${id}`
  );
};

const getExhibitionImage = async (exhibitionId: number, imageId: number) => {
  return await getBlob(
    `${baseUrl}/exhibition/image?exhibitionId=${exhibitionId}&imageId=${imageId}`
  );
};

const uploadImageToExhibition = async (
  exhibitionId: number,
  imageName: string,
  file: Blob
) => {
  return await postBlob(
    `${baseUrl}/exhibition/image?exhibitionId=${exhibitionId}&name=${imageName}`,
    file
  );
};

const deleteExhibition = async (exhibitionId: number) => {
  return await deleteRequest(
    `${baseUrl}/exhibition?exhibitionId=${exhibitionId}`
  );
};

const deleteExhibitionImage = async (imageId: string) => {
  return await deleteRequest(`${baseUrl}/exhibition/image?imageId=${imageId}`);
};

export const eruaMemberService = Object.freeze({
  getFriendList,
  createSolution,
  getAllSolutions,
  getEruaMember,
  deleteSolution,
  getSolutionsByMember,
  getProblemsBySolution,
  addSolutionToProblem,
  getUnlinkedProblemsBySolution,
  createExhibition,
  getExhibition,
  getExhibitions,
  getExhibitionImage,
  uploadImageToExhibition,
  deleteExhibition,
  deleteExhibitionImage,
});

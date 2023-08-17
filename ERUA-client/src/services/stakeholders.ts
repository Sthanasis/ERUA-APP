import {
  IAllSolutionsResponse,
  IEruaMemberSolution,
  IProblemPayload,
  IStakeholder,
  IStakeholderProblem,
} from '~/types/responseData';
import { deleteRequest, get, post } from './base';
import { useUserStore } from '~/pinia/modules/user';

const baseUrl = '/stakeholder';

const getAllStakeholders = () => {
  return get<IStakeholder[]>(baseUrl);
};

const createProblem = (payload: IProblemPayload) => {
  const { email } = useUserStore();
  return post<unknown>(`${baseUrl}/${email}/problems`, payload);
};

const getAllProblems = async (search?: string) => {
  let endpoint = `${baseUrl}/problems`;
  if (search) {
    endpoint = `${endpoint}?contain=${search}`;
  }
  return await get<IAllSolutionsResponse<IStakeholderProblem[]>>(endpoint);
};

const deleteProblem = async (problemId: number) => {
  return await deleteRequest(`${baseUrl}/problems?problemId=${problemId}`);
};

const getProblemsByMember = async (email: string) => {
  return await get<IAllSolutionsResponse<IStakeholderProblem[]>>(
    `${baseUrl}/${email}/problems`
  );
};

const getSolutionsByProblem = async (problemId: number) => {
  return await get<IEruaMemberSolution[]>(
    `${baseUrl}/problems/${problemId}/solutions`
  );
};

const addProblemToSolution = async (solutionId: number, problemId: number) => {
  return await get(
    `${baseUrl}/addproblemtosolution?solutionId=${solutionId}&problemId=${problemId}`
  );
};

const getUnlinkedSolutionsByProblem = async (problemId: number) => {
  return await get<IEruaMemberSolution[]>(
    `${baseUrl}/problems/notlinkedsolutions?problemId=${problemId}`
  );
};

export const stakeholderService = Object.freeze({
  getAllStakeholders,
  createProblem,
  getAllProblems,
  deleteProblem,
  getProblemsByMember,
  getSolutionsByProblem,
  addProblemToSolution,
  getUnlinkedSolutionsByProblem,
});
